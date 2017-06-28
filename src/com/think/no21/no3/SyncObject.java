package com.think.no21.no3;
/**
 * 在其他对象上同步
 * 
 * DualSync.f()(通过同步整个方法)在this同步，而g()有一个在syncObject上同步
 * 的synchronized块。因此，这两个同步是互相独立的。
 * 
 * @create @author Henry @date 2016-12-06 
 */
class DualSynch {
	private Object syncObject = new Object();

	public synchronized void f() {
		for (int i = 0; i < 5; i++) {
			System.out.print("f()");
			Thread.yield();
		}
	}

	public void g() {
		synchronized (syncObject) {
			for (int i = 0; i < 5; i++) {
				System.out.print("g()");
				Thread.yield();
			}
		}
	}
}
/**
 * 通过main()中创建调用f()的Thread对这一点进行了演示，因为main()线程是被用来调用g()的。
 * 从输出中可以看到，这两个方式在同时运行，因此任何方法都没有因为对另一个方法的
 * 同步而被阻塞。
 * 
 * @create @author Henry @date 2016-12-06
 *
 */
public class SyncObject {
	/**
	 * 运行结果可能是：
	 * g()f()g()f()g()f()g()f()g()f()
	 * @param args
	 */
	public static void main(String[] args) {
		final DualSynch ds = new DualSynch();
		new Thread() {
			public void run() {
				ds.f();
			};
		}.start();
		ds.g();
	}
}
