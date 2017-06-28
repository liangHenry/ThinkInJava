package com.think.no21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 你应该意识到后台进程在不执行finally子句的情况下就会终止其run()方法.
 * 此例子说明，当其中一个后台线程结束的时候，其他的后台线程是不能终止的。
 * @create @author Henry @date 2016-11-18
 *
 */
class ADeamon implements Runnable {
	private static int taskCount = 0;
	private final int id = taskCount++;
	@Override
	public void run() {
		try {
			System.out.println(id+" Starting ADaemon");
			if(id!=1){
				TimeUnit.SECONDS.sleep(3);
			}
		} catch (InterruptedException e) {
			System.out.println("Exiting via InterruptedExecption");
		}finally{
			System.out.println(id+" This should always run?");
		}
	}
}
/**
 * 当你运行这个程序时，你将看到finally子句就不会执行，
 * 但是如果你注释掉对setDaemon()的调用，就会看到finally子句将会执行。
 * 
 * @create @author Henry @date 2016-11-18
 *
 */
public class DaemonsDontRunFinally {
	/**
	 * 
	 * @create @author Henry @date 2016-11-18
	 * 运行结果如下：
	 * Starting ADaemon
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 3; i++) {
//			Thread t=new Thread(new ADeamon());
//			t.setDaemon(true);
//			t.start();
//		}
		ExecutorService exec=Executors.newCachedThreadPool();
		for (int i = 0; i < 3; i++)
			exec.execute(new ADeamon());
		TimeUnit.SECONDS.sleep(2);
		exec.shutdown();
		TimeUnit.SECONDS.sleep(10);
		System.out.println("hello");
	}
}
