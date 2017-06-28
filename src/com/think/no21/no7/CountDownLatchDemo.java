package com.think.no21.no7;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * CountDownLatch 
 * ��������ͬ��һ����������ǿ�����ǵȴ�����������ִ�е�һ�������ɡ�
 * 
 * �������CountDownLatch�����趨һ����ʼ����ֵ���κ�����������ϵ���wait()�ķ����������������ֵ��
 * CountDownLatch�����Ϊֻ����һ�Σ�����ֵ���ܱ����á��������Ҫ�ܹ����ü���ֵ�İ汾�������ʹ��
 * CyclicBarrier��
 * 
 * ����countDown()�������ڲ����������ʱ��û�б�������ֻ�ж�await()�ĵ��ûᱻ������ֱ������ֵ����0.
 * 
 * CountDownLatch�ĵ����÷��ǽ�һ�������Ϊn����������Ŀɽ�����񣬲�����ֵΪ0��CountDownLatch��
 * ��ÿ���������ʱ������������������ϵ���countDown()���ȴ����ⱻ���������������������ϵ���await(),
 * �������Լ���ס��ֱ��������������������������ʾ���ּ�����һ�����ʾ����
 */
/**
 * TaskPortion�����������һ��ʱ�䣬��ģ���ⲿ�ֹ��������
 * 
 * @create @author Henry @date 2016-12-29
 */
// Performs some portion of a task;
class TaskPortion implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private static Random rand = new Random(47);
	private final CountDownLatch latch;

	public TaskPortion(CountDownLatch latch) {
		this.latch = latch;
	}

	public void doWork() throws InterruptedException {
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
		System.out.println(this + "completed");
	}

	@Override
	public void run() {
		try {
			doWork();
			latch.countDown();
		} catch (InterruptedException e) {
			System.err.println("InterruptedException");
		}
	}

	@Override
	public String toString() {
		return String.format("%1$-3d", id);
	}
}
/**
 * WaitingTask��ʾϵͳ�б���ȴ��Ĳ��֣���Ҫ�ȴ�������ĳ�ʼ�������Ϊֹ��
 * 
 * @create @author Henry @date 2016-12-29
 */
// Waits on the CountDownLatch:
class WaitingTask implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private final CountDownLatch latch;

	public WaitingTask(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			latch.await();
			System.out.println("Latch barrier passed for " + this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return String.format("WaitingTask %1$-3d", id);
	}
}
/**
 * ��������ʹ������main()�ж����ͬһ����һ��CountDownLatch
 * 
 * @create @author Henry @date 2016-12-29
 */
public class CountDownLatchDemo {
	static final int SIZE=10;
	public static void main(String[] args) {
		ExecutorService exec=Executors.newCachedThreadPool();
		//All must share a single CountDownLatch object;
		CountDownLatch latch =new CountDownLatch(SIZE);
		for(int i=0;i<SIZE/2;i++)
			exec.execute(new WaitingTask(latch));
		for(int i=0;i<SIZE;i++)
			exec.execute(new TaskPortion(latch));
		System.out.println("Lauched all tasks");
		exec.shutdown();//Quit when all tasks complete
	}
}
