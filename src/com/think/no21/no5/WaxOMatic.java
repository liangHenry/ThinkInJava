package com.think.no21.no5;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 让我们看一个简单的示例，WaxOMatic.java有两个过程：一个是将蜡涂到Car上，
 * 一个是抛光它。抛光任务在涂蜡任务完成之前，是不能执行其工作的，而涂蜡任务
 * 在涂另一层蜡之前，必须等待抛光任务完成。WaxOn和WaxOff都使用了Car对象，
 * 该对象在这些任务等待条件变化的时候，使用wait()和notifyAll()来挂起和重
 * 新启动这些任务：
 */
/**
 * Car 有一个单一的布尔属性waxOn,表示涂蜡-抛光处理的状态。
 * 在waitForWaxing()中将检查waxOn标志，如果它为false,那么这个调用任务将通过调用
 * wait()而被挂起。这个行为发生在synchronized方法中这一点很重要，因为在这样的方法中，
 * 任务已经获得了锁。当你调用wait()时，线程被挂起，而锁被释放。锁被释放这一点的本质所在，
 * 因为为了安全地改变对象的状态（例如，将waxOn改变为true,如果挂起的任务要继续执行，
 * 就必须执行该动作）其他某个任务就必须能够获得这个锁。在本例中，如果另一个任务调用waxed()来表示
 * “是时候该干点什么了”，那么久必须获得这个锁，从而将waxOn改变为true。之后，waxed()调用notifyAll(),
 * 这将唤醒在对wait()的调用中被挂起的任务。为了使该任务从wait()中唤醒，它必须首先重新获得当它进入
 * wait()时释放的锁。在这个锁变得可用之前，这个任务是不会被唤醒的。
 * 
 * 
 * @create @author Henry @date 2016-12-06
 */
class Car {
	private boolean waxOn = false;

	public synchronized void waxed() {
		waxOn = true;// Ready to buff
		notifyAll();
	}

	public synchronized void buffed() {
		waxOn = false;// Ready for another coat of wax
		notifyAll();
	}
	public synchronized void waitForWaxing() throws InterruptedException {
		while (waxOn == false)
			wait();
	}

	public synchronized void waitForBuffing() throws InterruptedException {
		while (waxOn == true)
			wait();
	}
}

/**
 * WaxOn.run()表示给汽车打蜡过程的第一个步骤，因此它将执行它的操作：调用sleep()以模拟需要涂蜡的
 * 时间，然后告知汽车涂蜡结束，并调用waitForBuffing()，这个方法会用一个wait()调用来挂起这个任务，
 * 直至WaxOff任务调用这辆汽车的buffed()，从而改变状态并调用notifyAll()为止。
 * 
 * @create @author Henry @date 2016-12-06
 */
class WaxOn implements Runnable {
	private Car car;

	public WaxOn(Car c) {
		this.car = c;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.print(" Wax On! ");
				TimeUnit.MICROSECONDS.sleep(200);
				car.waxed();
				car.waitForBuffing();
			}
		} catch (InterruptedException e) {
			System.out.println("Exiting Wax On via interrupt");
		}
		System.out.println("Ending Wax On task");
	}
}
/**
 * WaxOff.run()立即进入waitForWaxing()，并以此而被挂起，直至WaxOn涂完蜡并且waxed()被调用。在运行这个程序时，
 * 你可以看到当控制权在这两个任务之间来回互相传递时，这个两步骤过程在不断地重复
 *  
 * @create @author Henry @date 2016-12-06
 */
class WaxOff implements Runnable {
	private Car car;
	public WaxOff(Car c){car=c;}
	@Override
	public void run() {
		try{
			while(!Thread.interrupted()){
				car.waitForWaxing();
				System.out.print(" Wax Off! ");
				TimeUnit.MILLISECONDS.sleep(200);
				car.buffed();
			}
		}catch (InterruptedException e) {
			System.out.println("Exiting Wax Off via interrupt");
		}
		System.out.println("Ending Wax Off task");
	}
}
/**
 * 在5秒钟之后，interrupt()会终止这两个线程；当你调用某个ExecutorService的shutdownNow()时，它会调用所有
 * 由它控制的线程的interrupt()。
 * 
 * @create @author Henry @date 2016-12-06
 */
public class WaxOMatic {
	public static void main(String[] args) throws InterruptedException {
		/*Car car=new Car();
		ExecutorService exec=Executors.newCachedThreadPool();
		exec.execute(new WaxOff(car));
		exec.execute(new WaxOn(car));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();*/
		System.out.println(new Date(1447264860000l));
		System.out.println(new Date(1447264717000l));
	}
}
