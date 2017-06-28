package com.think.no21.no3;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**线程本地存储 
 * 
 * 防止任务在共享资源上产生冲突的第二种是根除对变量的共享。线程本地存储是一种自动化机制，
 * 可以为使用相同变量的每个不同的线程都创建不同的存储。因此如果你有5个线程都要使用变量 x
 * 所表示的对象，那线程本地存储就会生成5个用于x的不同存储块。主要是，它们使得你可以将状态
 * 与线程关联起来。
 * 创建和管理线程本地存储可以由java.lang.ThreadLocal类实现，如下所示：
 * 
 * 
 * @create @author Henry @date 2016-12-06 
 *
 */
class Accessor implements Runnable{
	private final int id;
	public Accessor(int idn){
		this.id=idn;
	}

	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			ThreadLocalVariableHolder.increment();
			System.out.println(this);
			Thread.yield();
		}
	}
	@Override
	public String toString() {
		return "#"+id+": "+ThreadLocalVariableHolder.get() ;
	}
	
}

/**
 * 
 * ThreadLocal对象通常当作静态域存储。在创建ThreadLocal时，你只能通过get()和set()方法
 * 来访问该对象的内容，其中，get()方法将返回与线程相关联的对象的副本，而set()会将参数到插入
 * 到为其线程存储的对象中，并返回存储中原有的对象。increment()和get()方法在
 * ThreadLocalVariableHolder中演示了这一点。注意，increment()和get()方法都不是
 * synchronized的，因为ThreadLocal保存不会出现竞争条件。
 * 
 * 当运行这个程序时，你可以看到每个单独的线程都被分配了自己的存储，因为它们每个都需要跟踪
 * 自己的计数器，即便只有一个ThreadLocalVariableHolder对象。
 * 
 * @create @author Henry @date 2016-12-06 
 *
 */
public class ThreadLocalVariableHolder {
	private static ThreadLocal<Integer> value=new ThreadLocal<Integer>(){
		private Random rand=new Random(47);
		protected synchronized Integer initialValue(){
			return rand.nextInt();
		}
	};
	public static void increment(){
		value.set(value.get()+1);
	}
	public static int get(){
		return value.get();
	}
	public static void main(String[] args) throws Exception {
		ExecutorService exec =Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) 
			exec.execute(new Accessor(i));
		TimeUnit.SECONDS.sleep(3);
		exec.shutdown();
	}
}
