package com.think.no21.no4;
/**
 * 被互斥所阻塞
 * 就像在Interrupting.java 中看到的，如果你尝试着在一个对象上调用其synchronized方法。
 * 而这个对象的锁已经被其他任务获得，那么调用任务将被挂起(阻塞)，直至这个锁获得。下面的
 * 示例说明了同一个互斥可以如何能被同一个任务多次获得。
 * 
 * @create @author Henry @date 2016-12-14
 *
 */
public class MultiLock {
	public synchronized void f1(int count) {
		if (count-- > 0) {
			System.out.println(" f1() calling f2() with count " + count);
			f2(count);
		}
	}

	public synchronized void f2(int count) {
		if (count-- > 0) {
			System.out.println(" f2() calling f1() with count " + count);
			f1(count);
		}
	}
	/**
	 * 在main()中创建了一个调用f1()的Thread,然后f1()和f2()互相调用直至count变为0.
	 * 由于这个任务已近在第一个对f1()的调用种获得了multiLock对象锁，因此同一个任务将在
	 * f2()的调用种再次获得这个锁，依次类推。这么做事有意义的，因为一个任务应该能够调用在
	 * 同一个对象中的其他的synchronized方法，而这个任务已经持有锁了。
	 * 
	 * 运行结果如下：
	 * f1() calling f2() with count 9
	 * f2() calling f1() with count 8
	 * f1() calling f2() with count 7
	 * f2() calling f1() with count 6
	 * f1() calling f2() with count 5
	 * f2() calling f1() with count 4
	 * f1() calling f2() with count 3
	 * f2() calling f1() with count 2
	 * f1() calling f2() with count 1
	 * f2() calling f1() with count 0
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final MultiLock multiLock = new MultiLock();
		new Thread() {
			public void run() {
				multiLock.f1(10);
			};
		}.start();
	}
}
