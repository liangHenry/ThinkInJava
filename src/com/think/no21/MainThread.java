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
 * �������ʹ���ǿ��Խ����򻮷�Ϊ�������ġ��������е�����ͨ��ʹ�ö��̻߳��ƣ�
 * ��Щ��������Ҳ����Ϊ�������е�ÿһ��������ִ���߳���������
 * һ���߳̾����ڽ����е�һ����һ��˳�����������ˣ��������̿���ӵ�ж������ִ�е�����
 * ������ĳ���ʹ��ÿ�����񶼺��������Լ���CPUһ������ײ�������з�CPUʱ�䣬��ͨ���㲻��Ҫ��������
 * 
 * @create @author Henry @date 2016-11-16 ���²鿴Java���˼���̲߳��֡�
 */
public class MainThread {
	/**
	 * 
	 * ������run������
	 * 
	 * @create @author Henry @date 2016-11-16
	 * ������£�
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
	 * ���丶��Thread��
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 90%������£�
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
	 * ����5�����߳�
	 * 
	 * ���˵����ͬ�����ִ�����̱߳���������ʱ������һ�����ֽ��������̵߳������Զ����Ƶġ�
	 * �������Ļ������ж�����������̵߳�������������Щ������֮��ĬĬ�طַ��̡߳�
	 * 
	 * ��main()����Thread����ʱ������û�в����κζ���Щ��������á���ʹ����ͨ����ʱ��
	 * ���������������˵��һ����ƽ����Ϸ��������ʹ��Threadʱ������Ͳ�ͬ�ˡ�
	 * ÿһ��Thread����ע�ᡱ�����Լ������ȷʵ��һ�����������ã����������������˳���run()������֮ǰ��
	 * ���������޷������������Դ�����п�������Щ����ȷʵ���е��˽�����
	 * ���һ���̻߳ᴴ��һ��������ִ���̣߳��ڶ�start()�ĵ������֮�����Ծɻ�������ڡ�
	 * 
	 * @create @author Henry @date 2016-11-16
	 * ����������£�
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
	 * Java SE5��java.util.concurrent���е�ִ����(Executor)��Ϊ�����Thread ����
	 * �Ӷ����˲�����̡�Excetor�ڿͻ��˺�����ִ��֮���ṩ��һ����Ӳ㣻
	 * ��ͻ���ֱ��ִ������ͬ������н����ִ������Executor����������첽�����ִ�У�
	 * ��������ʽ�ع����̵߳��������ڡ�Executor��JavaSE5/6��������������ȷ�����
	 * CachedThreadPool�ڳ���ִ�й�����ͨ���ᴴ��������������ͬ���̣߳�
	 * Ȼ���ڻ��վ��߳�ʱֹͣ�������̣߳�������Ǻ����Executor����ѡ��
	 * ֻ�е����ַ�ʽ�����������ǣ������Ҫ�л���FixedTreadPool��
	 * 
	 * @create @author Henry @date 2016-11-16
	 * 
	 * ִ�п��ܽ�����£�
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
	 * ����Ժ����׵ؽ�ǰ��ʵ���е�CachedThreadPool�滻Ϊ��ͬ���͵�Executor.
	 * FixedThreadPoolʹ�������޵��̼߳���ִ�����ύ������
	 * ����FixedThreadPool����Ϳ���һ����Ԥ��ִ�д��۸߰����̷߳��䣬
	 * ���Ҳ�Ϳ��������̵߳������ˡ�����Խ�ʡʱ�䣬��Ϊ�㲻��Ϊÿ�����񶼹̶��ظ���
	 * �����̵߳Ŀ��������¼�������ϵͳ�У���Ҫ�̵߳��¼���������ͨ��ֱ�Ӵӳ��л�ȡ�̣߳�
	 * Ҳ����������Ը�ؾ���õ������㲻�����ÿɻ�õ���Դ����ΪFixedThreadPoolʹ�õ�
	 * Thread������������н�ġ�
	 * SingleThreadExecutor�����߳�����Ϊ1��FixedThreadPool.
	 * 
	 * @create @author Henry @date 2016-11-16
	 * ִ�п��ܽ�����£�
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
	 * ��Future���շ���ֵ��
	 * 
	 * @create @author Henry @date 2016-11-16
	 * ���������£�
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
	 * ���н�����£�
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
	 * �����߳����ȼ�
	 * 
	 * @create @author Henry @date 2016-11-16
	 * ���н�����£�
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
	 * ֻҪ���κηǺ�̨�̻߳������У�����Ͳ�����ֹ��
	 * ���磺main()�ľ���һ���Ǻ�̨�߳�
	 * 
	 * @create @author Henry @date 2016-11-16
	 * ���н�����£�
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
	 * ͨ����д���Ƶ�ThreadFactory���Զ�����Executor�����߳����ԡ�
	 * 
	 * @create @author Henry @date 2016-11-16
	 * ���н�����£�
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
		return "#" + id + "(" + (countDown > 0 ? countDown : "Liftoff") + "), ";
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
 * 
 * ����������ֵ���߳��ࡣ
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
 * Ӱ��������Ϊ��һ�ּ򵥷����ǵ���sleep(),�⽫ʹ������ִֹ�и�����ʱ�䡣
 * ��LiftOff���У�Ҫ�ǰѶ�yield()�ĵ��û����ǵ���sleep()
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
 * �����̵߳����ȼ�����
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
 * ��̨�̣߳���ָ�ڳ������е�ʱ���ٺ�̨�ṩ��һ�ַ�����̣߳�����
 * �����̲߳����ڳ����в��ɻ�ȱ�Ĳ��֡���ˣ������еķǺ�̨�߳̽���ʱ��
 * ����Ҳ����ֹ�ˣ�ͬʱ��ɱ�������е����к�̨�̡߳�
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
 * ����ThreadFactory,����̨״̬ȫ������Ϊtrue
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
 * ����һ���µ�DeamonFromFactory
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
 * ÿ����̬��ExecutorService����������������Ϊ����һ��ThreadFactory����
 * ��������󽫱����������µ��̣߳�
 * @create @author Henry @date 2016-11-16
 *
 */
class DaemonThreadPoolExecutor extends ThreadPoolExecutor {
	public DaemonThreadPoolExecutor() {
		super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),new DaemonThreadFactory());
	}
}
