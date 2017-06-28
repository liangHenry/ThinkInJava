package com.think.no21.no7;
/**
 * 
 * @author zhaoliang
 * @date 2017-5-23
 * 
 * Ϊ�˴���һ��ʾ�������ǿ���ʹ��Fat������һ�ִ������ɸ߰��Ķ������ͣ���Ϊ���Ĺ��������������ܺ�ʱ��
 */
public class Fat {
	private volatile double d;//prevent optimization
	private static int counter=0;
	private final int id=counter++;
	public Fat(){
		//Expensive , interruptible operation;
		for(int i=1;i<10000;i++)
			d+=(Math.PI+Math.E)/(double)i;
	}
	public void operation(){
		System.out.println(this);
	}
	public String toString(){
		return "Fat id : "+id;
	}

}
