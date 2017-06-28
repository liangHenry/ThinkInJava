package com.think.no21.no8;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * java ���߳�֮����
 * 
 * ���г���Ա����
 * 
 * �������ķ�����Ա�ʾ�κ������������͵��������������س��֣�
 * ����Ҫ���������޵ķ������ṩ��������ķ���ʱ�䡣ͨ�������������ȷ������ķ�����������
 * 
 * �ڱ����У�ÿ�����й˿�Ҫ��һ�������ķ���ʱ�䣬��ʱ����Ա���뻨���ڹ˿����ϣ�
 * �Է���˿������ʱ�䵥λ��������
 * ����ʱ���������ÿ���˿���˵���ǲ�ͬ�ģ����������ȷ���ġ�
 * ���⣬�㲻֪����ÿ��ʱ�������ж��ٹ˿ͻᵽ������Ҳ�����ȷ���ġ�
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
