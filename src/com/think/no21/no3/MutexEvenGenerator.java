package com.think.no21.no3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MutexEvenGenerator�����һ����������õ�������ʹ��lock()��unlock()����
 * ��next()�ڲ��������ٽ���Դ��������ʹ��Lock����ʱ����������ʾ�Ĺ��÷��ڲ����Ǻ���Ҫ�ģ�
 * �����Ŷ�lock()�ĵ��ã�����������finally�Ӿ��д���unlock()��try-finally����С�
 * ע�⣬return��������try�Ӿ��г��֣���ȷ��unlock()������緢�����Ӷ������ݱ�¶����
 * �ڶ�������
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
public class MutexEvenGenerator extends IntGenerator {
	private int currentEvenValue = 0;
	private Lock lock=new ReentrantLock();
	/**
	 * ����try-finally����Ĵ����synchronized�ؼ���Ҫ�࣬����
	 * ��Ҳ��������ʾ��Lock������ŵ�֮һ�����ʹ��synchronized�ؼ���ʱ��
	 * ĳЩ����ʧ���ˣ���ô�ͻ��׳�һ���쳣��������û�л���ȥ���κ���������
	 * ��ά��ϵͳʹ�䴦������״̬��������ʽ��Lock������Ϳ���ʹ��finally
	 * �Ӿ佫ϵͳά������ȷ��״̬�ˡ�
	 * 
	 * �����ϣ�����ʹ��sunchronized�ؼ���ʱ����Ҫд�Ĵ��������٣������û��������
	 * �Ŀ�����Ҳ�ή�ͣ���Ϊͨ��ֻ���ڽ����������ʱ����ʹ����ʽ��Lock����
	 * ���磬��synchronized�ؼ��ֲ��ܳ����Ż�ȡ�������ջ�ȡ����ʧ�ܣ����߳�����
	 * ��ȡ��һ��ʱ�䣬Ȼ���������Ҫʵ����Щ�������ʹ��concurrent��⡣
	 * 
	 * 
	 * @create @author Henry @date 2016-11-24
	 */
	@Override
	public int next() {
		lock.lock();
		try{
			++currentEvenValue;//Danger point here!
			Thread.yield();
			++currentEvenValue;
			return currentEvenValue;
		}finally{
			lock.unlock();
		}
	}
	public static void main(String[] args) {
		EvenChecker.test(new MutexEvenGenerator());
	}

}
