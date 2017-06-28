package com.think.no21.no4;

import java.util.concurrent.TimeUnit;

/**
 * ����ж�
 * 
 * ע�⣬�������߳��ϵ���interrupt()ʱ���жϷ�����Ψһʱ����������Ҫ���뵽���������У�
 * �����Ѿ������������ڲ�ʱ���������������˲����жϵ�I/O��������synchronized����֮�⣬
 * ���������������£����޿����£�������������ݳ������еĻ��������Ѿ���д�˿��ܻ����
 * �����������õĴ��룬���ָ���ô���أ������ֻ��ͨ���������������׳��쳣���˳�����ô��
 * ���޷����ǿ����뿪run()ѭ������ˣ���������interrupt()��ֹͣĳ��������ô��run()
 * ѭ������û�в����κ��������õ�����£����������Ҫ�ڶ��ַ�ʽ���˳���
 * ���ֻ��������ж�״̬����ʾ�ģ���״̬����ͨ������interrupt()�����á������ͨ������
 * interrupted()������е�״̬���ⲻ�����Ը�����interrupt()�Ƿ񱻵��ù������һ��������
 * �ж�״̬������ж�״̬����ȷ�������ṹ�����ĳ�������ж��������֪ͨ�����Σ�����Ծ���
 * ��һ��InterruptedException��һ�ĳɹ���Thread.interrupted()�������õ�����֪ͨ��
 * �����Ҫ�ٴμ�����˽��Ƿ��жϣ�������ڵ���Thread.interrupted()ʱ������洢������
 * �����ʾ��չʾ�˵��͵Ĺ��÷�����Ӧ����run()������ʹ�����������ж��߳�״̬������ʱ��
 * �������Ͳ��������ĸ��ֿ��ܡ�
 */
/**
 * NeedsCleanup��ǿ�����㾭���쳣�뿪ѭ��ʱ����ȷ������Դ�ı�Ҫ�ԡ�
 * 
 * @create @author Henry @date 2016-12-15
 * 
 */
class NeedsCleanup {
	private final int id;

	public NeedsCleanup(int ident) {
		id = ident;
		System.out.println("NeedsCleanup " + id);
	}

	public void cleanup() {
		System.out.println("Cleaning up " + id);
	}
}
/**
 * ע��,������Blocked3.run()�д�����NeedsCleanup��Դ����������������try-finally�Ӿ�,
 * ��ȷ��cleanup()�������ǻᱻ���á�
 * 
 * @create @author Henry @date 2016-12-15
 * 
 */
class Blocked3 implements Runnable {
	private volatile double d = 0.0;

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// point1
				NeedsCleanup n1 = new NeedsCleanup(1);
				// Start try-finally immediately after definition
				// of n1. to guarantee proper cleanup of n1;
				try {
					System.out.println("Sleeping");
					TimeUnit.SECONDS.sleep(1);
					// point2
					NeedsCleanup n2 = new NeedsCleanup(2);
					try {
						// Guarantee proper cleanup of n2;
						System.out.println("Calculating");
						// A time-consuming. non-blocking operation:
						for (int i = 0; i < 2500000; i++)
							d = d + (Math.PI + Math.E) / d;
						System.out.println("Finished time-consuming operation");
					} finally {
						n2.cleanup();
					}
				} finally {
					n1.cleanup();
				}
				System.out.println("Exiting via while() test");
			}
		} catch (InterruptedException e) {
			System.out.println("Exiting via InpterruptedException");
		}
	}
}
/**
 * ͨ��ʹ�ò�ͬ���ӳ٣�������ڲ�ͬ�ص��˳�Blocked3.run():��������sleep()�����У������ڷ�������
 * ��ѧ�����С��㽫���������interrupt()��ע��point2֮�󣨼��ڷ������Ĳ��������У������ã�
 * ��ô����ѭ����������Ȼ�����б��ض��󽫱����٣����ѭ���ᾭ��while���Ķ����˳������ǣ�
 * ���interrupt()��point1��point2֮�䣨��while���֮�󣬵�������������sleep()֮ǰ��������У�
 * �����ã���ô�������ͻ��ڵ�һ����ͼ������������֮ǰ������InterruptedException�˳���
 * ����������£����쳣���׳�֮ʱΨһ������������NeedsCleanup���󽫱����������Ҳ��������catch
 * �Ӿ���ִ�������κ���������Ļ��ᡣ
 * �����������Ӧinterrupt()������뽨��һ�ֲ��ԣ���ȷ��������һ�µ�״̬����ͨ����ζ��������Ҫ
 * ����Ķ��󴴽������ĺ��棬���������try-finally�Ӿ䣬�Ӷ�ʹ������run()ѭ������˳���������
 * �������������Ĵ���Ṥ���úܺã����ǣ�����������Java��ȱ���Զ��Ĺ��������ã�����⽫�����ڿͻ���
 * ����Աȥ��д��ȷ��try-finally�Ӿ䡣
 * 
 * 
 * @create @author Henry @date 2016-12-15
 * 
 */
public class InterruptingIdiom {
	public static void main(String[] args) throws Exception {
		Thread t = new Thread(new Blocked3());
		t.start();
		TimeUnit.MILLISECONDS.sleep(1900);
		t.interrupt();
	}
}
