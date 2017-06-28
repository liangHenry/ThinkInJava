package com.think.no21.no7;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * CountDownLatch 
 * 它被用来同步一个或多个任务，强制他们等待由其他任务执行的一组操作完成。
 * 
 * 你可以向CountDownLatch对象设定一个初始计数值，任何在这个对象上调用wait()的方法都减少这个计数值。
 * CountDownLatch被设计为只触发一次，计数值不能被重置。如果你需要能够重置计数值的版本，则可以使用
 * CyclicBarrier。
 * 
 * 调用countDown()的任务在产生这个调试时并没有被阻塞，只有对await()的调用会被阻塞，直至计数值到达0.
 * 
 * CountDownLatch的典型用法是将一个程序分为n个互相独立的可解决任务，并创建值为0的CountDownLatch。
 * 当每个任务完成时，都会在这个锁存器上调用countDown()。等待问题被解决的任务在这个锁存器上调用await(),
 * 将它们自己拦住，直至锁存器计数结束。下面是演示这种技术的一个框架示例：
 */
/**
 * TaskPortion将随机地休眠一段时间，以模拟这部分工作的完成
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
 * WaitingTask表示系统中必须等待的部分，它要等待到问题的初始部分完成为止。
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
 * 所有任务都使用了在main()中定义的同一个单一的CountDownLatch
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
