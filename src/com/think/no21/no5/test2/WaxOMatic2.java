package com.think.no21.no5.test2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ��Car�Ĺ������У�������Lock������һ��Condition�������������������������ͨ�š����ǣ����Condition
 * ���󲻰����κ��йش���״̬����Ϣ���������Ҫ�������ı�ʾ����״̬����Ϣ����boolean waxOn��
 * 
 * ÿ����lock()�ĵ��ö��������һ��try-finally�Ӿ䣬������֤����������¶������ͷ�������ʹ���ڽ��汾ʱ��
 * �����ڿ��Ե���await()��signal()��signalAll()֮ǰ������ӵ���������
 * 
 * ע�⣬������������ǰһ�����Ӹ��ӣ��ڱ��������ָ����Բ�δʹ���ջ���ࡣLock��Condition����ֻ���ڸ������ѵ�
 * ���߳������в��Ǳ���ġ�
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
 * WaxOn.run()��ʾ�������������̵ĵ�һ�����裬�������ִ�����Ĳ���������sleep()��ģ����ҪͿ����
 * ʱ�䣬Ȼ���֪����Ϳ��������������waitForBuffing()�������������һ��wait()�����������������
 * ֱ��WaxOff�����������������buffed()���Ӷ��ı�״̬������notifyAll()Ϊֹ��
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
 * WaxOff.run()��������waitForWaxing()�����Դ˶�������ֱ��WaxOnͿ��������waxed()�����á��������������ʱ��
 * ����Կ���������Ȩ������������֮�����ػ��ഫ��ʱ���������������ڲ��ϵ��ظ�
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
