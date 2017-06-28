package com.think.no21.no5;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ���й�Java���̻߳��Ƶ������У���һ�����������������notify()�����ѡ��������ڵȴ������񡱡����Ƿ���ζ���ڳ���
 * ���κεط����κδ���wait()״̬�е����񶼽����κζ�notifyAll()�ĵ��û����أ��������ʾ���У���Task2��ص�
 * ����˵��������������---��ʵ�ϣ���notifyAll()��ĳ���ض�����������ʱ��ֻ�еȴ������������Żᱻ���ѡ�
 * 
 * 
 * @create @author Henry @date 2016-12-21
 * 
 */

class Blocker {
	private String name;

	public Blocker(String name) {
		this.name = name;
	}

	synchronized void waitingCall() {
		try {
			while (!Thread.interrupted()) {
				wait();
				System.out.println(Thread.currentThread() + " " + name);
			}
		} catch (InterruptedException e) {
			// OK to exit this way
			System.out.println("InterruptedException");
		}
	}

	synchronized void prod() {
		notify();
	}

	synchronized void prodAll() {
		notifyAll();
	}
}
/**
 * Task �����Լ���Blocker����
 * 
 * @create @author Henry @date 2016-12-21
 * 
 */
class Task implements Runnable {
	static Blocker blocker = new Blocker("Task");

	@Override
	public void run() {
		blocker.waitingCall();
	}
}
/**
 * Task2 �����Լ���Blocker����
 * 
 * @create @author Henry @date 2016-12-21
 * 
 */
class Task2 implements Runnable {
	// A separate Blocker object;
	static Blocker blocker = new Blocker("Task2");

	@Override
	public void run() {
		blocker.waitingCall();
	}
}
/**
 * Task��Task2ÿ���������Լ���Blocker�������ÿ��Task���󶼻���Task.blocker����������ÿ��Task2������
 * Taks2.blocker����������main()�У�java.util.Timer����������Ϊÿ4/10��ִ��һ��run()�����������run()
 * ���������ɡ������������������Task.blocker�ϵ���notify()��notifyAll()��
 * �����������Կ�������ʹ����Task2.blocker��������Task2����Ҳû���κ���Task.blocker�ϵ�notify()��
 * notifyAll()���ûᵼ��Task2���󱻻��ѡ��ڴ����ƣ���main()�Ľ�β��������timer��cancel(),��ʹ��ʱ���������ˣ�
 * ǰ5������Ҳ��Ȼ�����У����Ծ������Ƕ�Task.blocker.waitingCall()�ĵ����б���������Task2.blocker.prodAll()
 * �ĵ���������������������κ���Task.blocker�е����ϵȴ�����
 * ��������Blocker�е�prod()��prodAll(),�ͻᷢ������������ġ���Щ������synchronized�ģ�����ζ�����ǽ���ȡ����
 * ��������˵����ǵ���notify()��notifyAll()ʱ��ֻ��������ϵ����Ƿ����߼���---��ˣ���ֻ�����ڵȴ�����ض���������
 * Blocker.waitingCall()�ǳ��򵥣��������ڱ����У���ֻ������for(;;)������while(!Thread.interrupted())�Ϳ��Ե���
 * ��ͬ��Ч������Ϊ�ڱ����У������쳣���뿪ѭ����ͨ�����interrupted()��־�뿪ѭ��ʱû���κ������---����������¶�Ҫ
 * ִ����ͬ�Ĵ��롣������ʵ�ϣ����ʾ��ѡ����interrupted()����Ϊ�����������뿪ѭ���ķ�ʽ��������Ժ��ĳ��ʱ�̣�
 * �����Ҫѭ������Ӹ���Ĵ��룬��ô���û�и��Ǵ����ѭ�����˳���������·�����ͻ�����������ķ��ա�
 * 
 * @create @author Henry @date 2016-12-21
 * 
 */
public class NotifyVsNotifyAll {
	public static void main(String[] args) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++)
			exec.execute(new Task());
		exec.execute(new Task2());
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			boolean prod = true;

			@Override
			public void run() {
				if (prod) {
					System.out.println("\nnotify()");
					Task.blocker.prod();
					prod = false;
				} else {
					System.out.println("\nnotifyAll()");
					Task.blocker.prodAll();
					prod = true;
				}
			}
		}, 400, 400);// Run every .4 second
		TimeUnit.SECONDS.sleep(5);
		timer.cancel();
		System.out.println("\n Timer canceled");

		TimeUnit.MILLISECONDS.sleep(500);
		System.out.println("Task2.blocker.prodAll()");
		Task2.blocker.prodAll();

		TimeUnit.MILLISECONDS.sleep(500);
		System.out.println("\n Shutting down");
		exec.shutdownNow();
	}
}
