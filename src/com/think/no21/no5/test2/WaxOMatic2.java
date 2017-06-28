package com.think.no21.no5.test2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 在Car的构造器中，单个的Lock将产生一个Condition对象，这个对象被用来管理任务间的通信。但是，这个Condition
 * 对象不包含任何有关处理状态的信息，因此你需要管理额外的表示处理状态的信息，即boolean waxOn。
 * 
 * 每个对lock()的调用都必须紧跟一个try-finally子句，用来保证在所有情况下都可以释放锁。在使用内建版本时，
 * 任务在可以调用await()、signal()或signalAll()之前，必须拥有这个锁。
 * 
 * 注意，这个解决方案比前一个更加复杂，在本例中这种复杂性并未使你收获更多。Lock和Condition对象只有在更加困难的
 * 多线程问题中才是必须的。
 * 
 * @create @author Henry @date 2016-12-22
 */
class Car {
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private boolean waxOn = false;

	public void waxed() {
		lock.lock();
		try {
			waxOn = true;// Ready to buff
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public void buffed() {
		lock.lock();
		try {
			waxOn = false;// Ready for another coat of wax
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public void waitForWaxing() throws InterruptedException {
		lock.lock();
		try {
			while (waxOn == false)
				condition.await();
		} finally {
			lock.unlock();
		}
	}

	public void waitForBuffing() throws InterruptedException {
		lock.lock();
		try {
			while (waxOn == true)
				condition.await();
		} finally {
			lock.unlock();
		}
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
			System.err.println("\nExiting Wax On via interrupt");
		}
		System.out.println("\nEnding Wax On task");
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

	public WaxOff(Car c) {
		car = c;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				car.waitForWaxing();
				System.out.print(" Wax Off! ");
				TimeUnit.MILLISECONDS.sleep(200);
				car.buffed();
			}
		} catch (InterruptedException e) {
			System.err.println("\nExiting Wax Off via interrupt");
		}
		System.out.println("\nEnding Wax Off task");
	}
}

public class WaxOMatic2 {
	public static void main(String[] args) throws Exception {
		Car car = new Car();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new WaxOff(car));
		exec.execute(new WaxOn(car));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
