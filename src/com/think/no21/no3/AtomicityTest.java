package com.think.no21.no3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**ÿ��ָ������һ��get��put������֮�仹��һЩ������ָ�����ڻ�ȡ�ͷ���֮�䣬
 * ��һ��������ܻ��޸���������ԣ���Щ��������ԭ���Եģ�
 * �����äĿ��Ӧ��ԭ���Ը����ô�ͻῴ������������е�getValue()���������������
 * �ó��򽵵�����ֵ����ֹ��
 * 
 * ����취getValue()��evenIncrement()������synchronized�ġ�
 * �������������£�ֻ�в���ר�Ҳ������������Ż������㻹��Ӧ������Brian��ͬ������
 * 
 * @create @author Henry @date 2016-11-28
 *
 */
public class AtomicityTest implements Runnable {
	/**
	 * ����iҲ����volatile�ģ���˻����ڿ��������⡣
	 */
	private int i=0;
	/**
	 * ����return i ȷʵ��ԭ���Բ���������ȱ��ͬ��ʹ������ֵ�������ڴ��ڲ��ȶ����м�
	 * ״̬ʱ����ȡ��
	 * @return
	 */
	public int getValue(){return i;}
	
	private synchronized void evenIncrement(){i++;i++;}
	
	@Override
	public void run() {
		while(true)
			evenIncrement();
	}
	public static void main(String[] args) {
		ExecutorService exec=Executors.newCachedThreadPool();
		AtomicityTest at=new AtomicityTest();
		exec.execute(at);
		while(true){
			int val=at.getValue();
			if(val%2!=0){
				System.out.println(val);
				System.exit(0);
			}
		}
	}
}
