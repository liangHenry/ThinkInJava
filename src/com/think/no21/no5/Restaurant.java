package com.think.no21.no5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * �����ߺ�������
 * 
 * �뿼������һ�����꣬����һ����ʦ��һ������Ա���������Ա����ȴ���ʦ׼������ʳ������ʦ׼����ʱ������֪ͨ����Ա��
 * ֮�����Ա�ϲˣ�Ȼ�󷵻ؼ����ȴ�������һ������Э����ʾ������ʦ���������ߣ�������Ա���������ߡ����������������ʳ��
 * ����������ʱ�������֣���ϵͳ����������ķ�ʽ�رա������Ƕ����������ģ�Ĵ��룺
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
 * �ȴ�Meal���ˣ����û��Meal�͵ȴ���ֱ����MealȻ�����ߣ�֪ͨ��ʦ��
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
 * ��ʦ�ฺ������������Meal�͵ȴ���û�о���һ��Meal��Ȼ��֪ͨWaitPerson��
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
 * Restaurant��WaitPerson��Chef�Ľ��㣬���Ƕ�����֪����Ϊ�ĸ�Restaurant��������Ϊ���Ǳ�������
 * ����ġ��ʹ����򽻵����Ա���û���ȥ��ʳrestaurant.meal����run()�У�WaitPerson����wait()ģʽ��
 * ֹͣ������ֱ����Chef��notifyAll()���ѡ���������һ���ǳ��򵥵ĳ����������ֻ֪��һ��������
 * WaitPerson�����ϵȴ�����WaitPerson���������������ԭ�������Ͽ��Ե���notify()������notifyAll()��
 * ���ǣ��ڸ����ӵ�����£����ܻ��ж��������ĳ���ض��������ϵȴ�������㲻֪���ĸ�����Ӧ�ñ����ѡ���ˣ�
 * ����notifyAll()Ҫ����ȫһЩ���������Ի��ѵȴ����˵�� �������񣬶�ÿ�����񶼱���������֪ͨ�Ƿ����Լ���ء�
 * 
 * һ��Chef����Meal��֪ͨWaitPerson�����Chef�ͽ��ȴ���ֱ��WaitPerson�ռ���������֪ͨChef��֮��Chef�Ϳ�����
 * ��һ��Meal�ˡ�
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
