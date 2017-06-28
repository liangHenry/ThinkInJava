package com.think.no21.no4;
/**
 * ������������
 * ������Interrupting.java �п����ģ�����㳢������һ�������ϵ�����synchronized������
 * �������������Ѿ������������ã���ô�������񽫱�����(����)��ֱ���������á������
 * ʾ��˵����ͬһ�������������ܱ�ͬһ�������λ�á�
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
	 * ��main()�д�����һ������f1()��Thread,Ȼ��f1()��f2()�������ֱ��count��Ϊ0.
	 * ������������ѽ��ڵ�һ����f1()�ĵ����ֻ����multiLock�����������ͬһ��������
	 * f2()�ĵ������ٴλ����������������ơ���ô����������ģ���Ϊһ������Ӧ���ܹ�������
	 * ͬһ�������е�������synchronized����������������Ѿ��������ˡ�
	 * 
	 * ���н�����£�
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
