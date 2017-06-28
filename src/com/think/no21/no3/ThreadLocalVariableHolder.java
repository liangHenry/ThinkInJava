package com.think.no21.no3;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**�̱߳��ش洢 
 * 
 * ��ֹ�����ڹ�����Դ�ϲ�����ͻ�ĵڶ����Ǹ����Ա����Ĺ����̱߳��ش洢��һ���Զ������ƣ�
 * ����Ϊʹ����ͬ������ÿ����ͬ���̶߳�������ͬ�Ĵ洢������������5���̶߳�Ҫʹ�ñ��� x
 * ����ʾ�Ķ������̱߳��ش洢�ͻ�����5������x�Ĳ�ͬ�洢�顣��Ҫ�ǣ�����ʹ������Խ�״̬
 * ���̹߳���������
 * �����͹����̱߳��ش洢������java.lang.ThreadLocal��ʵ�֣�������ʾ��
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
 * ThreadLocal����ͨ��������̬��洢���ڴ���ThreadLocalʱ����ֻ��ͨ��get()��set()����
 * �����ʸö�������ݣ����У�get()�������������߳�������Ķ���ĸ�������set()�Ὣ����������
 * ��Ϊ���̴߳洢�Ķ����У������ش洢��ԭ�еĶ���increment()��get()������
 * ThreadLocalVariableHolder����ʾ����һ�㡣ע�⣬increment()��get()����������
 * synchronized�ģ���ΪThreadLocal���治����־���������
 * 
 * �������������ʱ������Կ���ÿ���������̶߳����������Լ��Ĵ洢����Ϊ����ÿ������Ҫ����
 * �Լ��ļ�����������ֻ��һ��ThreadLocalVariableHolder����
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
