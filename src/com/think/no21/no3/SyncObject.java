package com.think.no21.no3;
/**
 * ������������ͬ��
 * 
 * DualSync.f()(ͨ��ͬ����������)��thisͬ������g()��һ����syncObject��ͬ��
 * ��synchronized�顣��ˣ�������ͬ���ǻ�������ġ�
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
 * ͨ��main()�д�������f()��Thread����һ���������ʾ����Ϊmain()�߳��Ǳ���������g()�ġ�
 * ������п��Կ�������������ʽ��ͬʱ���У�����κη�����û����Ϊ����һ��������
 * ͬ������������
 * 
 * @create @author Henry @date 2016-12-06
 *
 */
public class SyncObject {
	/**
	 * ���н�������ǣ�
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
