package com.think.no21.no4;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 以下的每个任务都表示了一种不同类型的阻塞。
 * SleepBlock是可中断的阻塞示例
 * 在run()中调用了sleep()
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
 * IOBlocked 是不可中断的阻塞示例。
 * 在I/O块上等待是不可中断的。
 * 在此类中调用了read()。Thread类的实例来实现的。
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
 * SynchronizedBlocked 是不可中断的阻塞示例。
 * 在synchronized块上的等待是不可中断的。
 * 这个匿名Thread类的对象通过调用f()获取了对象锁
 * (这个线程必须有别于为SynchronizedBlocked驱动run()的线程，因为一个线程可以多次获得某个对象锁)
 * 由于f()永远都不返回，因此这个锁永远不会释放。而SynchronizedBlock.run()在试图调用f()，
 * 并阻塞以等待这个锁被释放。
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
 * 从输出中可以看到，你能够中断对sleep()的调用(或者任何要求抛出InterruptedException的调用)。
 * 但是，你不能呢中断正在试图获取synchronized锁或者执行I/O具有锁住你的多线程程序的潜在可能。
 * 特别是对于基于Web的程序，这更是关乎利害。
 * 
 * 输出结果如下：
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
