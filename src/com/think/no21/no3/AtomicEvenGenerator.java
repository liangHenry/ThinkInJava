package com.think.no21.no3;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * ��AtomicInteger��д��MutexEvenGenerator
 * Atomic�౻�����������java.util.concurrent�е��࣬���ֻ������������²�
 * ���Լ��Ĵ�����ʹ�����ǣ�����ʹ����Ҳ����Ҫ��֤�������������ܳ��ֵ����⡣
 * ͨ����������Ҫ����ȫһЩ(Ҫô��synchronized�ؼ��֣�Ҫ������ʾ��Lock����)
 * 
 * @create @author Henry @date 2016-11-30
 *
 */
public class AtomicEvenGenerator extends IntGenerator {
	private AtomicInteger currentEvenValue=new AtomicInteger(0);
	
	@Override
	public int next() {
		return currentEvenValue.addAndGet(2);
	}
	/**
	 * ����������ʽ��ͬ���ٴ�ͨ��ʹ��AtomicInteger�õ��˸�����
	 * @create @author Henry @date 2016-11-30
	 * @param args
	 */
	public static void main(String[] args) {
		EvenChecker.test(new AtomicEvenGenerator());
	}
}
