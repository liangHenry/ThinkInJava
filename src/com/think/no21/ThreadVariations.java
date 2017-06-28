package com.think.no21;

import java.util.concurrent.TimeUnit;

/**
 * InnerThread1 ������һ����չ��Thread �������ڲ��࣬
 * �����ڹ������д�����һ���ڲ����һ��ʵ��������ڲ���
 * ��������������������Ҫ���ʵ������������·�����������ô
 * ������������塣���ǣ��ڴ����ʱ�򣬴����̵߳�ԭ��ֻ��Ϊ
 * ��ʹ��Thread����������˲���Ҫ���������ڲ��ࡣ
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
 * InnerThread2չʾ�˿��滻�ķ�ʽ���ڹ������д���һ��������Thread���࣬
 * ���ҽ�������ת��ΪThread����t.������е�����������Ҫ����t,�����ǿ���
 * ͨ��Thread�ӿ���ʵ�֣����Ҳ���Ҫ�˽�ö����ȷ�����͡�
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
 * �ο�InnerThread1
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
 * �ο�InnerThread2
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
 * ThreadMethod��չʾ���ڷ����ڲ���δ����̡߳�����׼���������߳�ʱ��
 * �Ϳ��Ե�����������������߳̿�ʼ֮�󣬸÷��������ء�������߳�ִֻ�и���������
 * �����Ǹ������Ҫ��������ô�����ڸ���Ĺ������ڲ������߳���ȣ�
 * ������һ�ָ������ö��ʺϵķ�ʽ��
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
 * �ڹ������������߳̿��ܻ��ú������⣬��Ϊ��һ��������ܻ��ڹ���������֮ǰ
 * ��ʼִ�У�����ζ�Ÿ������ܹ����ʴ��ڲ��ȶ�״̬�Ķ�����������Executor����
 * ����ʽ�ش���Thread�������һ��ԭ��
 * ��ʱͨ��ʹ���ڲ��������̴߳������������н�������ã�������������:
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
