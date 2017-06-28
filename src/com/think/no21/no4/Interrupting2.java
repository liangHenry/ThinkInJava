package com.think.no21.no4;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 就像前面在不可中断的I/O中所观察到的那样，无论在任何时刻，只要任务以不可中断的方式呗阻塞，
 * 那么都有潜在的会锁住程序的可能。Java SE5并发类库中添加了一个特性，即在ReentrantLock上
 * 阻塞的任务具备可以被中断的能力，这与在Synchronized 方法或临界区上阻塞的任务完全不同。
 */
/**
 * BlockedMutex类有一个构造器，它要获取所创建对象上自身的Lock，并且从不释放这个锁。
 * 出于这个原因，如果你试图从第二个任务中调用f()(不同于创建这个BlockedMutex的任务)，
 * 那么将会总是因Mutex不可获得而被阻塞。
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
 * 在Blocked2中，run()方法总是在调用blocked.f()的地方停止。
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
 * 当运行这个程序时，你将会看到，与I/O调用不同，interrupt()可以打断被互斥所阻塞的调用。
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
