package com.think.no21;

import java.util.concurrent.TimeUnit;

/**
 * InnerThread1 创建了一个扩展自Thread 的匿名内部类，
 * 并且在构造器中创建了一个内部类的一个实例。如果内部类
 * 具有你在其他方法中需要访问的特殊能力（新方法），那这么
 * 做将会很有意义。但是，在大多数时候，创建线程的原因只是为
 * 了使用Thread的能力，因此不必要创建匿名内部类。
 * 
 * @create @author Henry @date 2016-11-18
 */
// using a named inner class:
class InnerThread1 {
	private int countDown = 5;
	private Inner inner;

	private class Inner extends Thread {
		public Inner(String name) {
			super(name);
			start();
		}

		@Override
		public void run() {
			try {
				while (true) {
					System.out.println(this);
					if (--countDown == 0)
						return;
					sleep(10);
				}
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}
		}

		@Override
		public String toString() {
			return getName() + ": " + countDown;
		}
	}

	public InnerThread1(String name) {
		inner = new Inner(name);
	}
}

/**
 * InnerThread2展示了可替换的方式：在构造器中创建一个匿名的Thread子类，
 * 并且将其向上转型为Thread引用t.如果类中的其他方法需要访问t,那它们可以
 * 通过Thread接口来实现，并且不需要了解该对象的确切类型。
 * 
 * @create @author Henry @date 2016-11-18
 * 
 */
// Using an anonymous inner class:
class InnerThread2 {
	private int countDown = 5;
	private Thread t;

	public InnerThread2(String name) {
		t = new Thread(name) {
			@Override
			public void run() {
				try {
					while (true) {
						System.out.println(this);
						if (--countDown == 0)
							return;
						sleep(10);
					}
				} catch (InterruptedException e) {
					System.out.println("sleep() interrupted");
				}
			}

			public String toString() {
				return getName() + ":" + countDown;
			};
		};
		t.start();
	}
}

/**
 * 参考InnerThread1
 * 
 * @create @author Henry @date 2016-11-18
 * 
 */
// Using a named Runnable implememtation
class InnerRunnable1 {
	private int countDown = 5;
	private Inner inner;

	private class Inner implements Runnable {
		Thread t;

		public Inner(String name) {
			t = new Thread(this, name);
			t.start();
		}

		@Override
		public void run() {
			try {
				while (true) {
					System.out.println(this);
					if (--countDown == 0)
						return;
					TimeUnit.MILLISECONDS.sleep(10);
				}
			} catch (InterruptedException e) {
				System.out.println("sleep() interrupted");
			}
		}

		@Override
		public String toString() {
			return t.getName() + ":" + countDown;
		}
	}

	public InnerRunnable1(String name) {
		inner = new Inner(name);
	}
}

/**
 * 参考InnerThread2
 * 
 * @create @author Henry @date 2016-11-18
 * 
 */
// Using an anonymous Runnable implementation:
class InnerRunnable2 {
	private int countDown = 5;
	private Thread t;

	public InnerRunnable2(String name) {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						System.out.println(this);
						if (--countDown == 0)
							return;
						TimeUnit.MILLISECONDS.sleep(10);
					}
				} catch (InterruptedException e) {
					System.out.println("sleep() interrupted");
				}
			}

			@Override
			public String toString() {
				return Thread.currentThread().getName() + ":" + countDown;
			}
		}, name);
		t.start();
	}
}

/**
 * ThreadMethod类展示了在方法内部如何创建线程。当你准备好运行线程时，
 * 就可以调用这个方法，而在线程开始之后，该方法将返回。如果该线程只执行辅助操作，
 * 而不是该类的重要操作，那么这与在该类的构造器内部启动线程相比，
 * 可能是一种更加有用而适合的方式。
 * 
 * @create @author Henry @date 2016-11-18
 * 
 */
// A separate method to run some code as a tack:
class ThreadMethod {
	private int countDown = 5;
	private Thread t;
	private String name;

	public ThreadMethod(String name) {
		this.name = name;
	}

	public void runTask() {
		if (t == null) {
			t = new Thread(name) {
				public void run() {
					try {
						while (true) {
							System.out.println(this);
							if (--countDown == 0)
								return;
							TimeUnit.MILLISECONDS.sleep(10);
						}
					} catch (InterruptedException e) {
						System.out.println("sleep() interrupted");
					}
				}

				public String toString() {
					return getName() + ":" + countDown;
				};
			};
			t.start();
		}
	}
}

/**
 * 在构造器中启动线程可能会变得很有问题，因为另一个任务可能会在构造器结束之前
 * 开始执行，这意味着该任务能够访问处于不稳定状态的对象。这是优先Executor而不
 * 是显式地创建Thread对象的另一个原因。
 * 有时通过使用内部类来将线程代码隐藏在类中将会很有用，就像如下这样:
 * 
 * @create @author Henry @date 2016-11-18
 */
public class ThreadVariations {

	public static void main(String[] args) {
		new InnerThread1("InnerThread1");
		new InnerThread2("InnerThread2");
		new InnerRunnable1("InnerRunnable1");
		new InnerRunnable2("InnerRunnable2");
		new ThreadMethod("ThreadMethod").runTask();
	}
}
