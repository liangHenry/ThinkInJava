package com.think.no21;
/**
 * ������߳��е��쳣
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
/**
 * �򵥵��쳣�߳���
 * 
 * @create @author Henry @date 2016-11-23
 *
 */
class ExceptionThread2 implements Runnable{
	@Override
	public void run() {
		Thread t=Thread.currentThread();
		System.out.println("run() by "+t);
		System.out.println("eh = "+t.getUncaughtExceptionHandler());;
		throw new RuntimeException();
	}
}
/**
 * Thread.UncaughtExceptionHandler��java SE5 �е��½ӿڣ�
 * ����������ÿ��Thread�����϶�����һ���쳣��������
 * Thread.UncaughtExceptionHandler.uncaughtException()
 * �����߳���δ������쳣���ٽ�����ʱ������
 * 
 * @create @author Henry @date 2016-11-23
 *
 */
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("caught "+ e);
	}
}
/**
 * Ϊ��ʹ���������������Ǵ�����һ�������͵�ThreadFactory,
 * ������ÿ���´�����Thread�����ϸ���һ��Thread.UncaughtExceptionHandler.
 * 
 * 
 * @create @author Henry @date 2016-11-23
 *
 */
class HandlerThreadFactory implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		System.out.println(this+" creating new Thread");
		Thread t=new Thread(r);
		System.out.println("created "+ t);
		t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		System.out.println("eh = "+t.getUncaughtExceptionHandler());
		return t;
	}
	
}
/**
 * ���ǽ�����������ݸ�Executors�����µ�ExecutorService�ķ���
 * 
 * @create @author Henry @date 2016-11-23
 *
 */
public class CaptureUncaughtException {
	/**
	 * �ڳ���������˶���ĸ��ٻ��ƣ�������֤�����������̻߳ᴫ�ݸ�UncaughtExceptionHandler;
	 * ���ڿ��Կ�����δ������쳣ʱͨ��uncaughtException������ġ�
	 * 
	 * ���н����
	 * com.think.no21.HandlerThreadFactory@47b480 creating new Thread
	 * created Thread[Thread-0,5,main]
	 * eh = com.think.no21.MyUncaughtExceptionHandler@10d448
	 * this
	 * run() by Thread[Thread-0,5,main]
	 * eh = com.think.no21.MyUncaughtExceptionHandler@10d448
	 * caught java.lang.RuntimeException
	 * 
	 * @param args
	 */
	public static void main1(String[] args) {
		ExecutorService exec=Executors.newCachedThreadPool(new HandlerThreadFactory());
		exec.execute(new ExceptionThread2());
		System.out.println("this");
		//exec.shutdown();
	}
	
	/**
	 * �����ʾ��ʹ������԰��վ��������������ô������������֪����Ҫ�ڴ����д���
	 * ʹ����ͬ���쳣����������ô���򵥵ķ�ʽ����Thread��������һ����̬�򣬲������
	 * ����������ΪĬ�ϵ�δ�����쳣��������
	 * ���������ֻ���ڲ������߳�ת�Ƶ�δ�����쳣������������²Żᱻ���á�
	 * ϵͳ�����߳�ר�а汾�����û�з��֣������߳����Ƿ�����ר�е�
	 * uncaughtException()���������Ҳû�У��ٵ���defaultUncaughtExceptionHandler
	 * 
	 * ���н����
	 * run() by Thread[pool-1-thread-1,5,main]
	 * eh = java.lang.ThreadGroup[name=main,maxpri=10]
	 * caught java.lang.RuntimeException
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		ExecutorService exec=Executors.newCachedThreadPool();
		exec.execute(new ExceptionThread2());
	}
}
