package com.think.no21.no7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue
 * 
 * ����һ���޽��BlockingQueue�����ڷ���ʵ����Delayed�ӿڵĶ���
 * ���еČ���ֻ�����䵽ʱ���ܴӶ�����ȡ�ߡ����ֶ���������ģ�����ͷ������ӳٵ��ڵ�ʱ�����
 * ���û���κ��ӳٵ��ڣ���ô�Ͳ������κ�ͷԪ�أ�����poll()������null������Ϊ�������㲻�ܽ�
 * null���õ����ֶ����У���
 * 
 * ������һ��ʾ�������е�Delayed��������������񣬶�DelayedTaskConsumer���������������
 * ������ʱ��������񣩴Ӷ�����ȡ����Ȼ����������ע�⣬����DelayQueue�ͳ�Ϊ�����ȼ����е�һ�ֱ��壺
 */
/**
 * DelayedTask����һ����Ϊsequence��List<DelayedTask>�������������񱻴�����˳��������ǿ��Կ���
 * �����ǰ���ʵ�ʷ�����˳��ִ�еġ�
 * 
 * Delayed�ӿ���һ��������ΪgetDelay()��������������֪�ӳٵ����ж೤ʱ�䣬�����ӳ��ڶ೤ʱ��֮ǰ�Ѿ����ڡ�
 * ���������ǿ������ȥʹ��TimeUnit�࣬�������ǲ������͡�������һ���ǳ�������࣬��Ϊ����Ժ����׵�
 * ת����λ�������κ����������磬delta��ֵʱ�Ժ���Ϊ��λ�洢�ģ�����java SE5�ķ���System.nanoTime()����
 * ��ʱ������������Ϊ��λ�ġ������ת��delta��ֵ������������������ĵ�λ�Լ���ϣ����ʲô��λ����ʾ��
 * ��������������
 * NANOSECONDS.convert(delta,MILLISECONDS);
 * 
 * ��getDelay()�У�ϣ��ʹ�õĵ�λ����Ϊunit�������ݽ����ģ���ʹ��������ǰʱ���봥��ʱ��֮��Ĳ�ת��Ϊ������
 * Ҫ��ĵ�λ��������ֻ����Щ��λ��ʲô�����ǲ������ģʽ��һ����ʾ����������ģʽ�У��㷨��һ��������Ϊ����
 * ���ݽ����ģ���
 * 
 * Ϊ������Delayed�ӿڻ��̳���Comparable�ӿڣ���˱���ʵ��compareTo()��ʹ����Բ�������ıȽϡ�toString()
 * ��summary()�ṩ�������ʽ������Ƕ�׵�EndSentinel���ṩ��һ�ֹر����������;�������������ǽ������Ϊ���е�
 * ���һ��Ԫ�ء�
 * 
 * @create @author Henry @date 2017-1-3
 */
class DelayedTask implements Runnable, Delayed {
	private static int counter = 0;
	private final int id = counter++;
	private final int delta;
	private final long trigger;
	protected static List<DelayedTask> sequence = new ArrayList<DelayedTask>();

	public DelayedTask(int delayInMilliseconds) {
		delta = delayInMilliseconds;
		trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta, TimeUnit.MILLISECONDS);
		sequence.add(this);
	}

	@Override
	public void run() {
		System.out.println(this + " ---");
	}

	@Override
	public int compareTo(Delayed o) {
		DelayedTask that = (DelayedTask) o;
		if (trigger < that.trigger)
			return -1;
		if (trigger > that.trigger)
			return 1;
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(trigger - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	@Override
	public String toString() {
		return String.format("[%1$-4d]", delta) + " Task " + id;
	}

	public String summary() {
		return "(" + id + ":" + delta + ")";
	}

	public static class EndSentinel extends DelayedTask {
		private ExecutorService exec;

		public EndSentinel(int delay, ExecutorService e) {
			super(delay);
			exec = e;
		}

		@Override
		public void run() {
			for (DelayedTask pt : sequence) {
				System.out.println(pt.summary() + " ++");
			}
			System.out.println();
			System.out.println(this + " Calling shutdownNow()");
			exec.shutdownNow();
		}
	}
}
/**
 * ע�⣬��ΪDelayedTaskConsumer������һ���������������Լ���Thread��������ʹ������߳������дӶ����л�ȡ
 * ����������������������ǰ��ն������ȼ���˳��ִ�еģ�����ڱ����в���Ҫ�����κε������߳�������DelayedTask.
 * 
 * @create @author Henry @date 2017-1-3
 */
class DelayedTaskConsumer implements Runnable {
	private DelayQueue<DelayedTask> q;

	public DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
		this.q = q;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted())
				q.take().run();
				//new Thread(q.take()).start();
		} catch (InterruptedException e) {
			System.err.println("InterruptedException");
		}
		System.out.println("Finished DelayedTaskConsumer");
	}
}

/**
 * ������п��Կ��������񴴽���˳���ִ��˳��û���κ�Ӱ�죬�����ǰ������������ӳ�˳��ִ�еġ�
 * 
 * @create @author Henry @date 2017-1-3
 */
public class DelayQueueDemo {
	public static void main(String[] args) {
		Random rand = new Random(47);
		ExecutorService exec = Executors.newCachedThreadPool();
		DelayQueue<DelayedTask> queue = new DelayQueue<DelayedTask>();
		// Fill with tasks that have random delays;
		for (int i = 0; i < 5; i++)
			queue.put(new DelayedTask(rand.nextInt(5000)));
		// Set the stopping point
		queue.add(new DelayedTask.EndSentinel(5000, exec));
		exec.execute(new DelayedTaskConsumer(queue));
	}
}
