package com.think.no21.no4;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * ���µ�ÿ�����񶼱�ʾ��һ�ֲ�ͬ���͵�������
 * SleepBlock�ǿ��жϵ�����ʾ��
 * ��run()�е�����sleep()
 * 
 * @create @author Henry @date 2016-12-13
 */
class SleepBlocked implements Runnable {

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}
		System.out.println("Exiting SleepBlocked.run()");
	}
}

/**
 * IOBlocked �ǲ����жϵ�����ʾ����
 * ��I/O���ϵȴ��ǲ����жϵġ�
 * �ڴ����е�����read()��Thread���ʵ����ʵ�ֵġ�
 * 
 * @create @author Henry @date 2016-12-13
 */
class IOBlocked implements Runnable {
	private InputStream in;

	public IOBlocked(InputStream is) {
		in = is;
	}

	@Override
	public void run() {
		try {
			System.out.println("Waiting for read():");
			in.read();
		} catch (IOException e) {
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Interrupted from blocked I/O");
			} else {
				throw new RuntimeException(e);
			}
		}
		System.out.println("Exiting IOBlocked.run()");
	}

}

/**
 * SynchronizedBlocked �ǲ����жϵ�����ʾ����
 * ��synchronized���ϵĵȴ��ǲ����жϵġ�
 * �������Thread��Ķ���ͨ������f()��ȡ�˶�����
 * (����̱߳����б���ΪSynchronizedBlocked����run()���̣߳���Ϊһ���߳̿��Զ�λ��ĳ��������)
 * ����f()��Զ�������أ�����������Զ�����ͷš���SynchronizedBlock.run()����ͼ����f()��
 * �������Եȴ���������ͷš�
 * 
 * @create @author Henry @date 2016-12-13
 * 
 */
class SynchronizedBlocked implements Runnable {
	public synchronized void f() {
		System.out.println("start f()");
		while (true)
			// Never releases lock
			Thread.yield();
	}

	public SynchronizedBlocked() {
		new Thread() {
			@Override
			public void run() {
				f();// Lock acquired by this thread
			}
		}.start();
	}

	@Override
	public void run() {
		System.out.println("Trying to call f()");
		f();
		System.out.println("Exiting SynchronizedBlocked.run()");
	}
}

/**
 * ������п��Կ��������ܹ��ж϶�sleep()�ĵ���(�����κ�Ҫ���׳�InterruptedException�ĵ���)��
 * ���ǣ��㲻�����ж�������ͼ��ȡsynchronized������ִ��I/O������ס��Ķ��̳߳����Ǳ�ڿ��ܡ�
 * �ر��Ƕ��ڻ���Web�ĳ�������ǹغ�������
 * 
 * ���������£�
 * Interrupting com.think.no21.no4.SleepBlocked
 * Interrupt set to com.think.no21.no4.SleepBlocked
 * InterruptedException
 * Exiting SleepBlocked.run()
 * Waiting for read():
 * Interrupting com.think.no21.no4.IOBlocked
 * Interrupt set to com.think.no21.no4.IOBlocked
 * start f()
 * Trying to call f()
 * Interrupting com.think.no21.no4.SynchronizedBlocked
 * Interrupt set to com.think.no21.no4.SynchronizedBlocked
 * Aborting with System.exit(0)
 * 
 * @create @author Henry @date 2016-12-13
 * 
 */
public class Interrupting {
	private static ExecutorService exec = Executors.newCachedThreadPool();

	static void test(Runnable r) throws InterruptedException {
		Future<?> f = exec.submit(r);
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interrupting " + r.getClass().getName());
		f.cancel(true);
		System.out.println("Interrupt set to " + r.getClass().getName());
	}

	public static void main(String[] args) throws Exception {
		test(new SleepBlocked());
		test(new IOBlocked(System.in));
		test(new SynchronizedBlocked());
		TimeUnit.SECONDS.sleep(3);
		System.out.println("Aborting with System.exit(0)");
		System.exit(0);// ....since last 2 interrupts failed
	}
}
