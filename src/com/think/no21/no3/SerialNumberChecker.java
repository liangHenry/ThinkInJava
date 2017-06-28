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
 * Ϊ�˲���SerialNumberGenerator,������Ҫ����ľ��ڴ�ļ�(Set),�Է���Ҫ���Ѻܳ���ʱ����̽�����⡣
 * ������ʾ��GircularSet�����˴洢int��ֵ���ڴ棬��������������������ʱ��������ֵ���ǳ�ͻ�Ŀ����Լ�С��
 * add()��contains()��������synchronized���Է�ֹ�̳߳�ͻ��
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
 * SerialNumberChecker����һ����̬��CircularSet�������������������������������⻹����һ����Ƕ��SerialChecker�࣬
 * ������ȷ����������Ψһ�ġ�ͨ����������������������������㽫�����ں�г�������ջ�õ��ظ�������������������е�ʱ��
 * �㹻���Ļ���Ϊ�˽��������⣬��nextSerialNumber()ǰ�������synchronized�ؼ��֡�
 * 
 * �Ի������͵Ķ�ȡ�͸�ֵ��������Ϊ�ǰ�ȫ��ԭ���Բ��������ǣ���������AtomicityTest.java�п������������ڲ��ȶ�״̬ʱ��
 * �Ծɺܿ���ʹ��ԭ���Բ������������ǡ�������������������Ǽ��ֶ�Σ�յģ������ǵ�����������ѭBrian��ͬ������
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
	 * ���н����
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
