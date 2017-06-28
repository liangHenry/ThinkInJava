package com.think.no21.no4;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ����ǰ���ڲ����жϵ�I/O�����۲쵽���������������κ�ʱ�̣�ֻҪ�����Բ����жϵķ�ʽ��������
 * ��ô����Ǳ�ڵĻ���ס����Ŀ��ܡ�Java SE5��������������һ�����ԣ�����ReentrantLock��
 * ����������߱����Ա��жϵ�������������Synchronized �������ٽ�����������������ȫ��ͬ��
 */
/**
 * BlockedMutex����һ������������Ҫ��ȡ�����������������Lock�����ҴӲ��ͷ��������
 * �������ԭ���������ͼ�ӵڶ��������е���f()(��ͬ�ڴ������BlockedMutex������)��
 * ��ô����������Mutex���ɻ�ö���������
 * 
 * @create @author Henry @date 2016-12-14
 *
 */
class BlockedMutex{
	private Lock lock=new ReentrantLock();
	public BlockedMutex(){
		//Acquire it right away. to demonstrate interruption
		//of a task blocked on a ReentrantLock;
		lock.lock();
	}
	public void f(){
		try{
			//This will never be avaiable to a second task
			lock.lockInterruptibly();//special call;
			System.out.println("Lock acquired in f()");
		}catch (InterruptedException e) {
			System.out.println("Interrupted from lock acquisition in f()");
		}
	}
}
/**
 * ��Blocked2�У�run()���������ڵ���blocked.f()�ĵط�ֹͣ��
 * 
 * 
 * @create @author Henry @date 2016-12-14
 *
 */
class Blocked2 implements Runnable{
	BlockedMutex blocked=new BlockedMutex();
	@Override
	public void run() {
		System.out.println("Waiting for f() in BlockedMutex");
		blocked.f();
		System.out.println("Broken out of blocked call");
	}
}
/**
 * �������������ʱ���㽫�ῴ������I/O���ò�ͬ��interrupt()���Դ�ϱ������������ĵ��á�
 * 
 * @create @author Henry @date 2016-12-14
 *
 */
public class Interrupting2 {
	public static void main(String[] args) throws Exception {
		Thread t=new Thread(new Blocked2());
		t.start();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Issuing t.interrupt();");
		t.interrupt();
	}
}
