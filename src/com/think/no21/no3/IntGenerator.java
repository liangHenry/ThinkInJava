package com.think.no21.no3;

/**
 * 说明: 其中一个任务产生偶数，而其他任务消费这些数字。
 * 这里，消费者任务的唯一工作就是检查偶数的有效性。
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
/**
 * 首先，我们定义EvenChecker，即消费者任务，因为它将在随后所有的
 * 示例中被复用。为了将EvenChecker与我们要试验的各种类型的生成器
 * 解耦，我们将创建一个名为IntGenerator的抽象类，它包含EvenChecker
 * 必须了解的必不可少的方法：即一个Next()方法，和一个可以执行撤销的方法。
 * 这个类没有实现Generator接口，因为它必须产生一个int,而泛型不支持基本类型的参数
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
public abstract class IntGenerator {
	private volatile boolean canceled = false;

	public abstract int next();
	
	public void cancel() {
		canceled = true;
	}

	public boolean isCanceled() {
		return canceled;
	}
}
