package com.think.no21;
/**
 * 捕获多线程中的异常
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
/**
 * 简单的异常线程类
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
 * Thread.UncaughtExceptionHandler是java SE5 中的新接口，
 * 它允许你在每个Thread对象上都附着一个异常处理器。
 * Thread.UncaughtExceptionHandler.uncaughtException()
 * 会在线程因未捕获的异常而临近死亡时被调用
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
 * 为了使用上述方法，我们创建了一个新类型的ThreadFactory,
 * 它将在每个新创建的Thread对象上附着一个Thread.UncaughtExceptionHandler.
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
 * 我们将这个工厂传递给Executors创建新的ExecutorService的方法
 * 
 * @create @author Henry @date 2016-11-23
 *
 */
public class CaptureUncaughtException {
	/**
	 * 在程序中添加了额外的跟踪机制，用来验证工厂创建的线程会传递给UncaughtExceptionHandler;
	 * 现在可以看到，未捕获的异常时通过uncaughtException来捕获的。
	 * 
	 * 运行结果：
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
	 * 上面的示例使得你可以按照具体情况逐个地设置处理器，如果你知道将要在代码中处处
	 * 使用相同的异常处理器，那么更简单的方式是在Thread类中设置一个静态域，并将这个
	 * 处理器设置为默认的未捕获异常处理器。
	 * 这个处理器只有在不存在线程转悠的未捕获异常处理器的情况下才会被调用。
	 * 系统会检查线程专有版本，如果没有发现，则检查线程组是否有其专有的
	 * uncaughtException()方法，如果也没有，再调用defaultUncaughtExceptionHandler
	 * 
	 * 运行结果：
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
