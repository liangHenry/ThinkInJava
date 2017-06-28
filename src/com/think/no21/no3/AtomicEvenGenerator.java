package com.think.no21.no3;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * 用AtomicInteger重写的MutexEvenGenerator
 * Atomic类被设计用来构建java.util.concurrent中的类，因此只有在特殊情况下才
 * 在自己的代码中使用它们，即便使用了也不需要保证不存在其他可能出现的问题。
 * 通常依赖于锁要更安全一些(要么是synchronized关键字，要是是显示的Lock对象)
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
	 * 所以其他形式的同步再次通过使用AtomicInteger得到了根除。
	 * @create @author Henry @date 2016-11-30
	 * @param args
	 */
	public static void main(String[] args) {
		EvenChecker.test(new AtomicEvenGenerator());
	}
}
