package com.think.no21.no7;
/**
 * 
 * @author zhaoliang
 * @date 2017-5-23
 * 
 * 为了创建一个示例，我们可以使用Fat，这是一种创建代缴高昂的对象类型，因为它的构件器运行起来很耗时：
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
