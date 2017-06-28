package com.think.no21;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 
 * 并发编程使我们可以将程序划分为多个分离的、独立运行的任务。通过使用多线程机制，
 * 这些独立任务（也被称为子任务）中的每一个都将由执行线程来驱动。
 * 一个线程就是在进程中的一个单一的顺序控制流，因此，单个进程可以拥有多个并发执行的任务，
 * 但是你的程序使得每个任务都好像有其自己的CPU一样。其底层机制是切分CPU时间，但通常你不需要考虑它。
 * 
 * @create @author Henry @date 2016-11-16 重新查看Java编程思想线程部分。
 */
public class MainThread {
	/**
	 * 
	 * 单独跑run方法，
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 结果如下：
	 * #0(9), #0(8), #0(7), #0(6), #0(5), #0(4), #0(3), #0(2), #0(1),
	 * #0(Liftoff),
	 * @param args
	 */
	public static void main1(String[] args) {
		LiftOff lauch = new LiftOff();
		lauch.run();
	}

	/**
	 * 
	 * 将其付给Thread跑
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 90%结果如下：
	 * Waiting for LiftOff
	 * #0(9), #0(8), #0(7), #0(6), #0(5), #0(4), #0(3), #0(2), #0(1),
	 * #0(Liftoff),
	 * @param args
	 */
	public static void main2(String[] args) {
		Thread thread = new Thread(new LiftOff());
		thread.start();
		System.out.println("Waiting for LiftOff");
	}

	/**
	 * 
	 * 启动5个子线程
	 * 
	 * 输出说明不同任务的执行在线程被换进换出时混在了一起。这种交换是由线程调度器自动控制的。
	 * 如果在你的机器上有多个处理器，线程调度器将会在这些处理器之间默默地分发线程。
	 * 
	 * 当main()创建Thread对象时，它并没有捕获任何对这些对象的引用。在使用普通对象时，
	 * 这对于垃圾回收来说是一场公平的游戏，但是在使用Thread时，情况就不同了。
	 * 每一个Thread都“注册”了它自己，因此确实有一个对它的引用，而且在它的任务退出其run()并死亡之前，
	 * 垃圾回收无法清除它。你可以从输出中看到，这些任务确实运行到了结束，
	 * 因此一个线程会创建一个单独的执行线程，在对start()的调用完成之后，它仍旧会继续存在。
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 结果可能如下：
	 * Waiting for LiftOff
	 * #1(9), #3(9), #0(9), #3(8), #2(9), #3(7), #1(8), #0(8), #2(8),
	 * #0(7),
	 * #4(9), #2(7), #0(6), #2(6), #3(6), #1(7), #4(8), #3(5), #1(6),
	 * #4(7),
	 * #0(5), #2(5), #3(4), #0(4), #1(5), #2(4), #4(6), #0(3), #2(3),
	 * #3(3),
	 * #4(5), #1(4), #0(2), #2(2), #4(4), #0(1), #3(2), #2(1), #1(3),
	 * #4(3),
	 * #0(Liftoff), #2(Liftoff), #3(1), #4(2), #1(2), #3(Liftoff),
	 * #1(1),
	 * #1(Liftoff), #4(1), #4(Liftoff),
	 * @param args
	 */
	public static void main3(String[] args) {
		for (int i = 0; i < 5; i++) {
			new Thread(new LiftOff()).start();
		}
		System.out.println("Waiting for LiftOff");
	}

	/**
	 * 
	 * Java SE5的java.util.concurrent包中的执行器(Executor)将为你管理Thread 对象，
	 * 从而简化了并发编程。Excetor在客户端和任务执行之间提供了一个间接层；
	 * 与客户端直接执行任务不同，这个中介对象将执行任务。Executor允许你管理异步任务的执行，
	 * 而无须显式地管理线程的生命周期。Executor在JavaSE5/6中启动任务的优先方法。
	 * CachedThreadPool在程序执行过程中通常会创建与所需数量相同的线程，
	 * 然后在回收旧线程时停止创建新线程，因此它是合理的Executor的首选。
	 * 只有当这种方式会引发问题是，你才需要切换到FixedTreadPool。
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 
	 * 执行可能结果如下：
	 * #4(9), #2(9), #0(9), #4(8), #0(8), #4(7), #0(7), #4(6), #0(6),
	 * #4(5), #0(5), #4(4), #0(4), #4(3), #0(3), #4(2), #1(9), #4(1),
	 * #4(Liftoff), #3(9), #0(2), #3(8), #1(8), #2(8), #0(1), #3(7),
	 * #0(Liftoff), #2(7), #1(7), #3(6), #2(6), #1(6), #3(5), #2(5),
	 * #1(5), #3(4), #2(4), #1(4), #3(3), #2(3), #1(3), #3(2), #3(1),
	 * #2(2), #2(1), #2(Liftoff), #1(2), #3(Liftoff), #1(1),
	 * #1(Liftoff),
	 * @param args
	 */
	public static void main4(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++)
			exec.execute(new LiftOff());
		exec.shutdown();
	}

	/**
	 * 
	 * 你可以很容易地将前面实例中的CachedThreadPool替换为不同类型的Executor.
	 * FixedThreadPool使用了有限的线程集来执行所提交的任务；
	 * 有了FixedThreadPool，你就可以一次性预先执行代价高昂的线程分配，
	 * 因而也就可以限制线程的数量了。这可以节省时间，因为你不用为每个任务都固定地付出
	 * 创建线程的开销。在事件驱动的系统中，需要线程的事件处理器，通过直接从池中获取线程，
	 * 也可以如你所愿地尽快得到服务。你不会滥用可获得的资源，因为FixedThreadPool使用的
	 * Thread对象的数量是有界的。
	 * SingleThreadExecutor就是线程数量为1的FixedThreadPool.
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 执行可能结果如下：
	 * #0(9), #3(9), #1(9), #2(9), #4(9), #1(8), #3(8), #1(7), #3(7),
	 * #1(6), #3(6), #1(5), #3(5), #1(4), #3(4), #1(3), #3(3), #1(2),
	 * #3(2), #1(1), #3(1), #4(8), #2(8), #0(8), #1(Liftoff), #2(7),
	 * #4(7), #0(7), #2(6), #3(Liftoff), #0(6), #4(6), #2(5), #0(5),
	 * #0(4), #2(4), #4(5), #0(3), #4(4), #0(2), #2(3), #4(3), #0(1),
	 * #2(2), #4(2), #0(Liftoff), #2(1), #4(1), #2(Liftoff),
	 * #4(Liftoff),
	 * @param args
	 */
	public static void main5(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++)
			exec.execute(new LiftOff());
		exec.shutdown();
	}

	/**
	 * 
	 * 用Future接收返回值：
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 输出结果如下：
	 * result of TaskWithResult 0
	 * result of TaskWithResult 1
	 * result of TaskWithResult 2
	 * result of TaskWithResult 3
	 * result of TaskWithResult 4
	 * result of TaskWithResult 5
	 * result of TaskWithResult 6
	 * result of TaskWithResult 7
	 * result of TaskWithResult 8
	 * result of TaskWithResult 9
	 * 
	 * @param args
	 */
	public static void main6(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>();
		for (int i = 0; i < 10; i++)
			results.add(exec.submit(new TaskWithResult(i)));
		for (Future<String> fs : results) {
			try {
				System.out.println(fs.isDone());
				System.out.println(fs.get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				exec.shutdown();

			}
		}
	}

	/**
	 * @create @author Henry @date 2016-11-16
	 * 
	 * 运行结果如下：
	 * #0(9), #2(9), #4(9), #1(9), #3(9), #4(8), #2(8), #0(8), #3(8),
	 * #1(8),
	 * #1(7), #0(7), #2(7), #4(7), #3(7), #1(6), #3(6), #0(6), #2(6),
	 * #4(6),
	 * #1(5), #4(5), #3(5), #2(5), #0(5), #3(4), #0(4), #2(4), #4(4),
	 * #1(4),
	 * #2(3), #4(3), #0(3), #3(3), #1(3), #3(2), #2(2), #1(2), #0(2),
	 * #4(2),
	 * #3(1), #4(1), #0(1), #2(1), #1(1), #3(Liftoff), #2(Liftoff),
	 * #1(Liftoff),
	 * #0(Liftoff), #4(Liftoff),
	 * @param args
	 */
	public static void main7(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++)
			exec.execute(new SleepingTask());
		exec.shutdown();
	}

	/**
	 * 
	 * 测试线程优先级
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 运行结果如下：
	 * Thread[pool-1-thread-6,10,main]:5
	 * Thread[pool-1-thread-3,1,main]:5
	 * Thread[pool-1-thread-1,1,main]:5
	 * Thread[pool-1-thread-5,1,main]:5
	 * Thread[pool-1-thread-6,10,main]:4
	 * Thread[pool-1-thread-2,1,main]:5
	 * Thread[pool-1-thread-4,1,main]:5
	 * Thread[pool-1-thread-6,10,main]:3
	 * Thread[pool-1-thread-3,1,main]:4
	 * Thread[pool-1-thread-1,1,main]:4
	 * Thread[pool-1-thread-5,1,main]:4
	 * Thread[pool-1-thread-6,10,main]:2
	 * Thread[pool-1-thread-2,1,main]:4
	 * Thread[pool-1-thread-4,1,main]:4
	 * Thread[pool-1-thread-6,10,main]:1
	 * Thread[pool-1-thread-2,1,main]:3
	 * Thread[pool-1-thread-4,1,main]:3
	 * Thread[pool-1-thread-3,1,main]:3
	 * Thread[pool-1-thread-1,1,main]:3
	 * Thread[pool-1-thread-5,1,main]:3
	 * Thread[pool-1-thread-2,1,main]:2
	 * Thread[pool-1-thread-4,1,main]:2
	 * Thread[pool-1-thread-3,1,main]:2
	 * Thread[pool-1-thread-4,1,main]:1
	 * Thread[pool-1-thread-2,1,main]:1
	 * Thread[pool-1-thread-1,1,main]:2
	 * Thread[pool-1-thread-5,1,main]:2
	 * Thread[pool-1-thread-3,1,main]:1
	 * Thread[pool-1-thread-1,1,main]:1
	 * Thread[pool-1-thread-5,1,main]:1
	 * 
	 * @param args
	 */
	public static void main8(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++)
			exec.execute(new SimplePriorities(Thread.MIN_PRIORITY));
		exec.execute(new SimplePriorities(Thread.MAX_PRIORITY));
		exec.shutdown();
	}

	/**
	 * 只要有任何非后台线程还在运行，程序就不会终止。
	 * 比如：main()的就是一个非后台线程
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 运行结果如下：
	 * All daemons started
	 * Thread[Thread-1,5,main] com.think.no21.SimpleDaemons@e09713
	 * Thread[Thread-2,5,main] com.think.no21.SimpleDaemons@19b49e6
	 * Thread[Thread-8,5,main] com.think.no21.SimpleDaemons@47b480
	 * Thread[Thread-4,5,main] com.think.no21.SimpleDaemons@156ee8e
	 * Thread[Thread-0,5,main] com.think.no21.SimpleDaemons@de6f34
	 * Thread[Thread-6,5,main] com.think.no21.SimpleDaemons@83cc67
	 * Thread[Thread-3,5,main] com.think.no21.SimpleDaemons@10d448
	 * Thread[Thread-5,5,main] com.think.no21.SimpleDaemons@e0e1c6
	 * Thread[Thread-7,5,main] com.think.no21.SimpleDaemons@6ca1c
	 * Thread[Thread-9,5,main] com.think.no21.SimpleDaemons@1bf216a
	 * Thread[Thread-2,5,main] com.think.no21.SimpleDaemons@19b49e6
	 * Thread[Thread-8,5,main] com.think.no21.SimpleDaemons@47b480
	 * Thread[Thread-4,5,main] com.think.no21.SimpleDaemons@156ee8e
	 * Thread[Thread-0,5,main] com.think.no21.SimpleDaemons@de6f34
	 * Thread[Thread-6,5,main] com.think.no21.SimpleDaemons@83cc67
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main9(String[] args) throws Exception {
		for (int i = 0; i < 10; i++) {
			Thread daemon = new Thread(new SimpleDaemons());
			daemon.setDaemon(true);
			daemon.start();
		}
		System.out.println("All daemons started");
		TimeUnit.MILLISECONDS.sleep(175);
	}

	/**
	 * 通过编写定制的ThreadFactory可以定制由Executor创建线程属性。
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 运行结果如下：
	 * All daemons started
	 * Thread[Thread-1,5,main] com.think.no21.DaemonFromFactory@109a4c
	 * Thread[Thread-3,5,main] com.think.no21.DaemonFromFactory@201f9
	 * Thread[Thread-5,5,main] com.think.no21.DaemonFromFactory@1cf8583
	 * Thread[Thread-2,5,main] com.think.no21.DaemonFromFactory@14693c7
	 * Thread[Thread-4,5,main] com.think.no21.DaemonFromFactory@901887
	 * Thread[Thread-6,5,main] com.think.no21.DaemonFromFactory@665753
	 * Thread[Thread-8,5,main] com.think.no21.DaemonFromFactory@ef22f8
	 * Thread[Thread-9,5,main] com.think.no21.DaemonFromFactory@4a65e0
	 * Thread[Thread-7,5,main] com.think.no21.DaemonFromFactory@3a6727
	 * Thread[Thread-0,5,main] com.think.no21.DaemonFromFactory@1e0cf70
	 * Thread[Thread-1,5,main] com.think.no21.DaemonFromFactory@109a4c
	 * Thread[Thread-3,5,main] com.think.no21.DaemonFromFactory@201f9
	 * Thread[Thread-5,5,main] com.think.no21.DaemonFromFactory@1cf8583
	 * Thread[Thread-7,5,main] com.think.no21.DaemonFromFactory@3a6727
	 * Thread[Thread-9,5,main] com.think.no21.DaemonFromFactory@4a65e0
	 * Thread[Thread-0,5,main] com.think.no21.DaemonFromFactory@1e0cf70
	 * Thread[Thread-6,5,main] com.think.no21.DaemonFromFactory@665753
	 * Thread[Thread-4,5,main] com.think.no21.DaemonFromFactory@901887
	 * Thread[Thread-2,5,main] com.think.no21.DaemonFromFactory@14693c7
	 * Thread[Thread-8,5,main] com.think.no21.DaemonFromFactory@ef22f8
	 * Thread[Thread-9,5,main] com.think.no21.DaemonFromFactory@4a65e0
	 * Thread[Thread-7,5,main] com.think.no21.DaemonFromFactory@3a6727
	 * Thread[Thread-8,5,main] com.think.no21.DaemonFromFactory@ef22f8
	 * Thread[Thread-2,5,main] com.think.no21.DaemonFromFactory@14693c7
	 * Thread[Thread-0,5,main] com.think.no21.DaemonFromFactory@1e0cf70
	 * Thread[Thread-6,5,main] com.think.no21.DaemonFromFactory@665753
	 * Thread[Thread-4,5,main] com.think.no21.DaemonFromFactory@901887
	 * Thread[Thread-5,5,main] com.think.no21.DaemonFromFactory@1cf8583
	 * Thread[Thread-3,5,main] com.think.no21.DaemonFromFactory@201f9
	 * Thread[Thread-1,5,main] com.think.no21.DaemonFromFactory@109a4c
	 * Thread[Thread-9,5,main] com.think.no21.DaemonFromFactory@4a65e0
	 * Thread[Thread-7,5,main] com.think.no21.DaemonFromFactory@3a6727
	 * Thread[Thread-5,5,main] com.think.no21.DaemonFromFactory@1cf8583
	 * Thread[Thread-8,5,main] com.think.no21.DaemonFromFactory@ef22f8
	 * Thread[Thread-2,5,main] com.think.no21.DaemonFromFactory@14693c7
	 * Thread[Thread-0,5,main] com.think.no21.DaemonFromFactory@1e0cf70
	 * Thread[Thread-6,5,main] com.think.no21.DaemonFromFactory@665753
	 * Thread[Thread-4,5,main] com.think.no21.DaemonFromFactory@901887
	 * Thread[Thread-3,5,main] com.think.no21.DaemonFromFactory@201f9
	 * Thread[Thread-1,5,main] com.think.no21.DaemonFromFactory@109a4c
	 * Thread[Thread-5,5,main] com.think.no21.DaemonFromFactory@1cf8583
	 * Thread[Thread-7,5,main] com.think.no21.DaemonFromFactory@3a6727
	 * Thread[Thread-9,5,main] com.think.no21.DaemonFromFactory@4a65e0
	 * Thread[Thread-3,5,main] com.think.no21.DaemonFromFactory@201f9
	 * Thread[Thread-1,5,main] com.think.no21.DaemonFromFactory@109a4c
	 * Thread[Thread-0,5,main] com.think.no21.DaemonFromFactory@1e0cf70
	 * Thread[Thread-2,5,main] com.think.no21.DaemonFromFactory@14693c7
	 * Thread[Thread-8,5,main] com.think.no21.DaemonFromFactory@ef22f8
	 * Thread[Thread-4,5,main] com.think.no21.DaemonFromFactory@901887
	 * Thread[Thread-6,5,main] com.think.no21.DaemonFromFactory@665753
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
		for (int i = 0; i < 10; i++)
			exec.execute(new DaemonFromFactory());
		System.out.println("All daemons started");
		TimeUnit.MILLISECONDS.sleep(500);
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
		return "#" + id + "(" + (countDown > 0 ? countDown : "Liftoff") + "), ";
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
 * 
 * 声明带返回值的线程类。
 * 
 * @create @author Henry @date 2016-11-16
 */
class TaskWithResult implements Callable<String> {
	private int id;

	public TaskWithResult(int id) {
		this.id = id;
	}

	@Override
	public String call() throws Exception {
		return "result of TaskWithResult " + id;
	}
}

/**
 * 
 * 影响人物行为的一种简单方法是调用sleep(),这将使任务中止执行给定的时间。
 * 在LiftOff类中，要是把对yield()的调用换成是调用sleep()
 * 
 * @create @author Henry @date 2016-11-16
 */
class SleepingTask extends LiftOff {
	@Override
	public void run() {
		try {
			while (countDown-- > 0) {
				System.out.print(status());
				// Old-style;
				// Thread.sleep(100);
				// java SE5/6-style;
				TimeUnit.MILLISECONDS.sleep(100);
			}
		} catch (InterruptedException e) {
			System.err.println("Interrupted");
		}
	}
}

/**
 * 
 * 测试线程的优先级的类
 * 
 * @create @author Henry @date 2016-11-16
 */
class SimplePriorities implements Runnable {
	private int countDown = 5;
	private volatile double d;// No optimization
	private int priority;

	public SimplePriorities(int priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return Thread.currentThread() + ":" + countDown;
	}

	@Override
	public void run() {
		Thread.currentThread().setPriority(priority);
		while (true) {
			// An expensive,interruptable operation:
			for (int i = 1; i < 100000; i++) {
				d += (Math.PI + Math.E) / (double) i;
				if (i % 1000 == 0)
					Thread.yield();
			}
			System.out.println(this);
			if (--countDown == 0)
				return;
		}
	}
}

/**
 * 后台线程，是指在程序运行的时候再后台提供的一种服务的线程，并且
 * 这种线程不属于程序中不可或缺的部分。因此，当所有的非后台线程结束时，
 * 程序也就终止了，同时会杀死进程中的所有后台线程。
 * 
 * @create @author Henry @date 2016-11-16
 * 
 */
class SimpleDaemons implements Runnable {

	@Override
	public void run() {
		try {
			while (true) {
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.println(Thread.currentThread() + " " + this);
			}
		} catch (InterruptedException e) {
			System.out.println("sleep() interrupted");
		}
	}
}

/**
 * 创建ThreadFactory,将后台状态全部设置为true
 * 
 * @create @author Henry @date 2016-11-16
 * 
 */
class DaemonThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return t;
	}
}

/**
 * 创建一个新的DeamonFromFactory
 * 
 * @create @author Henry @date 2016-11-16
 * 
 */
class DaemonFromFactory implements Runnable {
	@Override
	public void run() {
		try {
			while (true) {
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.println(Thread.currentThread() + " " + this);
			}
		} catch (InterruptedException e) {
			System.out.println("interrupted");
		}
	}
}
/**
 * 每个静态的ExecutorService创建方法都被重载为接受一个ThreadFactory对象，
 * 而这个对象将被用来创建新的线程：
 * @create @author Henry @date 2016-11-16
 *
 */
class DaemonThreadPoolExecutor extends ThreadPoolExecutor {
	public DaemonThreadPoolExecutor() {
		super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),new DaemonThreadFactory());
	}
}
