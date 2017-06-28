package com.think.no21.no5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 生产者和消费者
 * 
 * 请考虑这样一个饭店，它有一个厨师和一个服务员。这个服务员必须等待厨师准备好膳食。当厨师准备好时，他会通知服务员，
 * 之后服务员上菜，然后返回继续等待。这是一个任务协作的示例：厨师代表生产者，而服务员代表消费者。两个任务必须在膳食被
 * 生产和消费时进行握手，而系统必须以有序的方式关闭。下面是对这个叙述建模的代码：
 * 
 * @create @author Henry @date 2016-12-21
 */

class Meal {
	private final int orderNum;

	public Meal(int orderNum) {
		this.orderNum = orderNum;
		System.out.println("create "+toString());
	}

	@Override
	public String toString() {
		return "Meal " + orderNum;
	}
}
/**
 * 等待Meal的人，如果没有Meal就等待，直到有Meal然后拿走，通知厨师。
 * 
 * @create @author Henry @date 2016-12-22
 */
class WaitPerson implements Runnable {
	private Restaurant restaurant;

	public WaitPerson(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					while (restaurant.meal == null)
						wait();// ... for the chef to produce a meal
				}
				System.out.println("Waitperson got " + restaurant.meal);
				synchronized (restaurant.chef) {
					restaurant.meal = null;
					restaurant.chef.notifyAll();//Ready for another
				}
			}
		} catch (InterruptedException e) {
			System.out.println("WaitPerson interrupted");
		}
	}
}
/**
 * 厨师类负责做饭，当有Meal就等待，没有就做一个Meal，然后通知WaitPerson。
 *  
 * @create @author Henry @date 2016-12-22
 */
class Chef implements Runnable {
	private Restaurant restaurant;
	private int count = 0;

	public Chef(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					while (restaurant.meal != null)
						wait(); // ...for the meal to be taken
				}
				if (++count == 10) {
					System.out.println("Out of food. closing");
					restaurant.exec.shutdownNow();
				}
				System.out.println("Order up! ");
				synchronized (restaurant.waitPerson) {
					restaurant.meal = new Meal(count);
					restaurant.waitPerson.notifyAll();
				}
				TimeUnit.MILLISECONDS.sleep(100);
			}
		} catch (InterruptedException e) {
			System.out.println("Chef interrupted");
		}
	}

}
/**
 * Restaurant是WaitPerson和Chef的焦点，他们都必须知道在为哪个Restaurant工作，因为他们必须和这家
 * 饭店的“餐窗”打交道，以便放置或拿去膳食restaurant.meal。在run()中，WaitPerson进入wait()模式，
 * 停止其任务，直至被Chef的notifyAll()唤醒。由于这是一个非常简单的程序，因此我们只知道一个任务将在
 * WaitPerson的锁上等待：即WaitPerson任务自身。出于这个原因，理论上可以调用notify()而不是notifyAll()。
 * 但是，在更复杂的情况下，可能会有多个任务在某个特定对象锁上等待，因此你不知道哪个任务应该被唤醒。因此，
 * 调用notifyAll()要更安全一些，这样可以唤醒等待这个说的 所有任务，而每个任务都必须决定这个通知是否与自己相关。
 * 
 * 一旦Chef送上Meal并通知WaitPerson，这个Chef就将等待，直至WaitPerson收集到订单并通知Chef，之后Chef就可以烧
 * 下一份Meal了。
 * 
 * @create @author Henry @date 2016-12-22
 */
public class Restaurant {
	Meal meal;
	ExecutorService exec = Executors.newCachedThreadPool();
	WaitPerson waitPerson = new WaitPerson(this);
	Chef chef = new Chef(this);

	public Restaurant() {
		exec.execute(chef);
		exec.execute(waitPerson);
	}

	public static void main(String[] args) {
		new Restaurant();
	}
}
