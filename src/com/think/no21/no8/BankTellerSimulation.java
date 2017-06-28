package com.think.no21.no8;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * java 多线程之仿真
 * 
 * 银行出纳员仿真
 * 
 * 这个经典的仿真可以表示任何属于这种类型的情况：对象随机地出现，
 * 并且要求数量有限的服务器提供随机数量的服务时间。通过构件仿真可以确定理想的服务器数量。
 * 
 * 在本例中，每个银行顾客要求一定数量的服务时间，这时出纳员必须花费在顾客身上，
 * 以服务顾客需求的时间单位的数量。
 * 服务时间的数量对每个顾客来说都是不同的，并且是随机确定的。
 * 另外，你不知道在每个时间间隔内有多少顾客会到达，因此这也是随机确定的。
 * 
 */
// Read-only objects don't require synchronization;
class Customer {
	private final int serviceTime;

	public Customer(int tm) {
		serviceTime = tm;
	}

	public int getServiceTime() {
		return serviceTime;
	}

	public String toString() {
		return "[" + serviceTime + "]";
	}
}

// Teach the customer line to display itself;
class CustomerLine extends ArrayBlockingQueue<Customer> {

	public CustomerLine(int maxLineSize) {
		super(maxLineSize);
	}

	public String toString() {
		if (this.size() == 0)
			return "[Empty]";
		StringBuilder result = new StringBuilder();
		for (Customer customer : this)
			result.append(customer);
		return result.toString();
	}
}

// Randomly and customers to a queue;
class CustomerGenerator implements Runnable {
	private CustomerLine customers;
	private static Random rand = new Random(47);

	public CustomerGenerator(CustomerLine cq) {
		customers = cq;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(rand.nextInt(300));
				customers.put(new Customer(rand.nextInt(1000)));
			}
		} catch (InterruptedException e) {
			System.err.println("CustomerGemerator interrupted");
		}
		System.out.println("CustomerGenerator terminating");
	}
}

class Teller implements Runnable, Comparable<Teller> {
	private static int counter = 0;
	private final int id = counter++;

	// customers served during this shift;
	private int customersServed = 0;
	private CustomerLine customers;
	private boolean servingCustomerLine = true;

	public Teller(CustomerLine cq) {
		customers = cq;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Customer customer = customers.take();
				TimeUnit.MILLISECONDS.sleep(customer.getServiceTime());
				synchronized (this) {
					customersServed++;
					while (!servingCustomerLine)
						wait();
				}
			}

		} catch (InterruptedException e) {
			System.err.println(this + " interrupted");
		}
		System.out.println(this + " terminating");
	}

	public synchronized void doSomethingElse() {
		customersServed = 0;
		servingCustomerLine = false;
	}

	public synchronized void serveCustomerLine() {
		assert !servingCustomerLine : "already serving: " + this;
		servingCustomerLine = true;
		notifyAll();
	}

	public String toString() {
		return "Teller " + id + "";
	}

	public String shortString() {
		return "T" + id;
	}
	//Used by priority queue:
	@Override
	public synchronized int compareTo(Teller o) {
		return customersServed < o.customersServed ? -1 : (customersServed == o.customersServed ? 0 : 1);
	}

}

public class BankTellerSimulation {

}
