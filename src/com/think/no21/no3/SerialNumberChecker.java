package com.think.no21.no3;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// : concurrency/SerialNumberChecker.java
// Operations that may seem safe are not.
// when threads are present.
// {Args:4}
/**
 * 为了测试SerialNumberGenerator,我们需要不会耗尽内存的集(Set),以防需要花费很长的时间来探测问题。
 * 这里所示的GircularSet重用了存储int数值的内存，并假设在你生成序列数时，产生数值覆盖冲突的可能性极小。
 * add()和contains()方法都是synchronized，以防止线程冲突。
 * 
 * Reuses storage so we don't run out of memory:
 * 
 * @create @author Henry @date 2016-11-29
 */
class CircularSet {
	private int[] array;
	private int len;
	private int index = 0;

	public CircularSet(int size) {
		array = new int[size];
		len = size;
		// Initialize to a value not produced
		// by the SerialNumberGenerator
		for (int i = 0; i < size; i++)
			array[i] = -1;
	}

	public synchronized void add(int i) {
		array[index] = i;
		// Wrap index and write over lod elements;
		index = ++index % len;
	}

	public synchronized boolean contains(int val) {
		for (int i = 0; i < len; i++)
			if (array[i] == val)
				return true;
		return false;
	}
}

/**
 * SerialNumberChecker包含一个静态的CircularSet，它持有所产生的所有序列数；另外还包含一个内嵌的SerialChecker类，
 * 它可以确保序列数是唯一的。通过创建多个任务来竞争序列数，你将发现在和谐任务最终会得到重复的序列数，如果你运行的时间
 * 足够长的话。为了解决这个问题，在nextSerialNumber()前面添加了synchronized关键字。
 * 
 * 对基本类型的读取和赋值操作被认为是安全的原子性操作。但是，正如你在AtomicityTest.java中看到，当对象处于不稳定状态时，
 * 仍旧很可能使用原子性操作来访问它们。对这个问题做出假设是棘手而危险的，最明智的做法就是遵循Brian的同步规则。
 * 
 * @create @author Henry @date 2016-11-29
 * 
 */
public class SerialNumberChecker {
	private static final int SIZE = 10;
	private static CircularSet serials = new CircularSet(1000);
	private static ExecutorService exec = Executors.newCachedThreadPool();

	static class SerialChecker implements Runnable {

		@Override
		public void run() {
			while (true) {
				int serial = SerialNumberGenerator.nextSerialNumber();
				if (serials.contains(serial)) {
					System.out.println("Duplicate: " + serial);
					System.exit(0);
				}
				serials.add(serial);
			}
		}
	}
	/**
	 * 运行结果：
	 * No duplicates detected
	 * Duplicate: 3920303
	 * Duplicate: 3920302
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < SIZE; i++) {
			exec.execute(new SerialChecker());
			// Stop after n seconds if there's an argument;
			if (true) {// args.length>0
				TimeUnit.SECONDS.sleep(new Integer("4"));// args[0]
				System.out.println("No duplicates detected");
				//System.exit(0);
			}
		}
	}
}
