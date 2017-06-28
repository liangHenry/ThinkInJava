package com.think.no21.no5;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * �����ǿ�һ���򵥵�ʾ����WaxOMatic.java���������̣�һ���ǽ���Ϳ��Car�ϣ�
 * һ�����׹������׹�������Ϳ���������֮ǰ���ǲ���ִ���乤���ģ���Ϳ������
 * ��Ϳ��һ����֮ǰ������ȴ��׹�������ɡ�WaxOn��WaxOff��ʹ����Car����
 * �ö�������Щ����ȴ������仯��ʱ��ʹ��wait()��notifyAll()���������
 * ��������Щ����
 */
/**
 * Car ��һ����һ�Ĳ�������waxOn,��ʾͿ��-�׹⴦���״̬��
 * ��waitForWaxing()�н����waxOn��־�������Ϊfalse,��ô�����������ͨ������
 * wait()�������������Ϊ������synchronized��������һ�����Ҫ����Ϊ�������ķ����У�
 * �����Ѿ�����������������wait()ʱ���̱߳����𣬶������ͷš������ͷ���һ��ı������ڣ�
 * ��ΪΪ�˰�ȫ�ظı�����״̬�����磬��waxOn�ı�Ϊtrue,������������Ҫ����ִ�У�
 * �ͱ���ִ�иö���������ĳ������ͱ����ܹ������������ڱ����У������һ���������waxed()����ʾ
 * ����ʱ��øɵ�ʲô�ˡ�����ô�ñ�������������Ӷ���waxOn�ı�Ϊtrue��֮��waxed()����notifyAll(),
 * �⽫�����ڶ�wait()�ĵ����б����������Ϊ��ʹ�������wait()�л��ѣ��������������»�õ�������
 * wait()ʱ�ͷŵ��������������ÿ���֮ǰ����������ǲ��ᱻ���ѵġ�
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
			System.out.println("Exiting Wax On via interrupt");
		}
		System.out.println("Ending Wax On task");
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
 * ��5����֮��interrupt()����ֹ�������̣߳��������ĳ��ExecutorService��shutdownNow()ʱ�������������
 * �������Ƶ��̵߳�interrupt()��
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
