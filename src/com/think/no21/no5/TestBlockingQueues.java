package com.think.no21.no5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * �������п��Խ���ǳ����������⣬���䷽ʽ��wait()��notifyAll()��ȣ���򵥲��ɿ��öࡣ
 * ������һ���򵥵Ĳ��ԣ��������LiftOff�����ִ�д��л��ˡ���������LiftOffRunner������ÿ��LiftOff�����BlockingQueue
 * ���Ƴ���ֱ�����С���������ͨ����ʽ�ص���run()��ʹ���Լ����߳������У�������Ϊÿ����������һ�����̡߳���
 * 
 * @create @author Henry @date 2016-12-23
 * 
 */
class LiftOffRunner implements Runnable {
	private BlockingQueue<LiftOff> rockets;

	public LiftOffRunner(BlockingQueue<LiftOff> rockets) {
		this.rockets = rockets;
	}

	public void add(LiftOff lo) {
		try {
			rockets.put(lo);
		} catch (InterruptedException e) {
			System.err.println("Interrupted during put()");
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("Coming in");
			while (!Thread.interrupted()) {
				LiftOff rocket;
				rocket = rockets.take();
				rocket.run();
			}
		} catch (InterruptedException e) {
			System.err.println("Waking from take() ,InterruptedException");
		}
		System.out.println("Exiting LiftOffRunner");
	}
}

/**
 * 
 * �򵥵Ķ���ʵ��Runnable�ӿ�
 * 
 * @create @author Henry @date 2016-11-16
 */
class LiftOff implements Runnable {
	protected int countDown = 10;
	private static int taskCount = 0;
	private final int id = taskCount++;

	public LiftOff() {
	}

	public LiftOff(int countDown) {
		this.countDown = countDown;
	}

	public String status() {
		return "#" + id + "(" + (countDown > 0 ? countDown + "), ": "Liftoff),\n") ;
	}

	@Override
	public void run() {
		while (countDown-- > 0) {
			System.out.print(status());
			/**
			 * Thread.yield()�ĵ����Ƕ��̵߳�����(java�̻߳��Ƶ�һ���֣����Խ�CPU��һ���߳�ת�Ƹ���һ���߳�)��һ�ֽ��飬
			 * ���������������Ѿ�ִ������������������Ҫ�Ĳ����ˣ��˿������л�����������ִ��һ��ʱ��Ĵ��ʱ������
			 * ����ȫ��ѡ���Եģ���������ʹ��������Ϊ��������Щʾ���в���������Ȥ�����������п��ܻῴ�����񻻽�������֤�ݡ�
			 * 
			 */
			Thread.yield();
		}
	}
}
/**
 * ����������main()���õ���BlockingQueue�У�������LiftOffRunner��BlockingQueue��ȡ����ע�⣬LiftOffRunner���Ժ���
 * ͬ�����⣬��Ϊ�����Ѿ���BlockingQueue����ˡ�
 * 
 * @create @author Henry @date 2016-12-23
 *
 */
public class TestBlockingQueues {
	static void getkey() {
		try {
			// Compensate for Windows/Linux difference in the
			// length of the result produced by the Enter key;
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static void getkey(String message) {
		System.out.println(message);
		getkey();
	}

	static void test(String msg, BlockingQueue<LiftOff> queue) throws InterruptedException {
		System.out.println(msg);
		LiftOffRunner runner = new LiftOffRunner(queue);
		Thread t = new Thread(runner);
		t.start();
		TimeUnit.SECONDS.sleep(2);
		for (int i = 0; i < 5; i++)
			runner.add(new LiftOff(5));
		TimeUnit.SECONDS.sleep(3);
		getkey("Press 'Enter' (" + msg + ")");
		t.interrupt();
		System.out.println("Finished " + msg + " test");
	}

	public static void main(String[] args) throws Exception {
		test("LinedBlockingQueue ",new LinkedBlockingQueue<LiftOff>());
		test("ArrayBlockingQueue ",new ArrayBlockingQueue<LiftOff>(3));
		test("SynchronousQueue ",new SynchronousQueue<LiftOff>());
	}
}
