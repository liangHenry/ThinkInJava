package com.think.no21;

/**
 * * join()方法解释：
 * 一个线程可以在其他线程之上调用Join()方法，其效果是等待一段时间直到第二个线程结束才继续执行。
 * 如果某个线程在另一个线程t上调用t.join()，此线程将被挂起，直到目标线程t结束才恢复
 * （即t.isAlive()返回为假）。
 * 也可以在调用join()时带上一个超时参数（单位可以是毫秒，或者毫秒和纳秒），
 * 这样如果目标线程在这段时间到期时还没有结束的话，join()方法总能返回。
 * 对join()方法的调用可以被中断，做法实是在调用线程上调用interrupt()方法，
 * 这时需要调用try-catch子句。
 */
/**
 * Sleeper 是一个Thread类型，它需要休眠一段时间，这段时间是通过构造器传进来的参数所指定的。
 * 在run()中，sleep()方法有可能在指定的时间期满时返回，但也可能被中断。
 * 在catch子句中，将根据isInterrpted()的返回值报告这个中断。当另一个线程在该线程上调用interrupt()时，
 * 将给该线程设定一个标志，表明该线程已经被中断。然而，异常被捕获时将清理这个标志，
 * 所以在catch子句中，在异常被捕获的时候这个标志总是为假。除异常之外，这个标志还可用于其他情况，
 * 比如线程可能会检查其中断状态。
 * 
 * @create @author Henry @date 2016-11-23
 * 
 */
class Sleeper extends Thread {
	private int duration;

	public Sleeper(String name, int sleepTime) {
		super(name);
		duration = sleepTime;
		start();
	}

	@Override
	public void run() {
		try {
			sleep(duration);
			System.out.println("world");
		} catch (InterruptedException e) {
			System.out.println(getName() + " was interrupted. IsInterrupted():" + isInterrupted());
			return;
		}
		System.out.println(getName() + " has awakened");
	}
}

/**
 * Joiner 线程将通过在Sleeper对象上调用join()方法来等待Sleeper醒来。
 * 
 * @create @author Henry @date 2016-11-23
 * 
 */
class Joiner extends Thread {
	private Sleeper sleeper;

	public Joiner(String name, Sleeper sleeper) {
		super(name);
		this.sleeper = sleeper;
		start();
	}

	@Override
	public void run() {
		try {
			System.out.println(sleeper.getName() + " join");
			sleeper.join();
		} catch (InterruptedException e) {
			System.out.println("Interrupted---");
		}
		System.out.println(getName() + " join completed");
	}
}

/**
 * 在main()里面，每个Sleeper都有一个Joiner,这个可以在输出中发现，
 * 如果sleeper被中断或者正常结束，Joiner将和Sleeper一同结束。
 * 
 * @create @author Henry @date 2016-11-23
 * 
 */
public class Joining {
	public static void main(String[] args) {
		Sleeper sleepy = new Sleeper("Sleepy", 1500), grumpy = new Sleeper("Grumpy", 1500);
		Joiner dopey = new Joiner("Dopey", sleepy), doc = new Joiner("Doc", grumpy);
		// System.out.println("hello");
		// grumpy.interrupt();
	}
}
