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
 * 阻塞队列可以解决非常大量的问题，而其方式与wait()和notifyAll()相比，则简单并可靠得多。
 * 下面是一个简单的测试，它将多个LiftOff对象的执行串行化了。消费者是LiftOffRunner，它将每个LiftOff对象从BlockingQueue
 * 中推出并直接运行。（即，它通过显式地调用run()而使用自己的线程来运行，而不是为每个任务启动一个新线程。）
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
 * 简单的对象实现Runnable接口
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
			 * Thread.yield()的调用是对线程调度器(java线程机制的一部分，可以将CPU从一个线程转移给另一个线程)的一种建议，
			 * 它在声明：“我已经执行完生命周期中最重要的部分了，此刻正是切换给其他任务执行一段时间的大好时机。”
			 * 这完全是选择性的，但是这里使用它是因为它会在这些示例中产生更加有趣的输出：你更有可能会看到任务换进换出的证据。
			 * 
			 */
			Thread.yield();
		}
	}
}
/**
 * 各个任务有main()放置到了BlockingQueue中，并且由LiftOffRunner从BlockingQueue中取出。注意，LiftOffRunner可以忽略
 * 同步问题，因为它们已经由BlockingQueue解决了。
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
