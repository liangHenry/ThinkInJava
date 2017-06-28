package com.think.no21.no3;
/**
 * 一个任务有可能在另一个任务执行第一个对currentEvenValue的递增操作之后，
 * 但是没有执行第二个操作之前，调用next()方法（即，代码中被注释为
 * "Danger point here!"的地方）。这将使这个值处于"不恰当"的状态。为了证明这是可能发生的，
 * EvenChecker.test()创建了一组EvenChecker对象，以连续地读取并输出同一个
 * EvenGenerator,并测试检查每个数值是否都是偶数。如果不是，就会报告错误，而程序也将关闭。
 * 
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
public class EventGenerator extends IntGenerator{
	private int currentEvenValue=0;
	/**
	 * 如果你正在写一个变量，它可能接下来将被另一个线程读取，
	 * 或者正在读取一个上次已经被另一个线程写过的变量，那么你必须使用同步，并且，
	 * 读取线程都必须使用相同的监视器锁同步。
	 * 
	 * @create @author Henry @date 2016-11-24
	 */
	@Override
	public int next() {//synchronized
		++currentEvenValue;//Danger point here!
		//Thread.yield();
		++currentEvenValue;
		return currentEvenValue;
	}
	/**
	 * 这个程序最终将失败，因为各个EvenChecker任务在EvenGenerator处于"不恰当的"状态时，
	 * 仍能够访问其中的信息。但是，更加你使用的特定操作系统和其他实现细节，直到
	 * EvenCenerator完成多次循环之前，这个问题都不会被探测到。如果你希望更快地发现
	 * 失败，可以尝试着将对yield()的调用放置在第一个和第二个递增操作之间。这只是并发程序的部分问题
	 * 如果失败的概率非常低，那么即使存在缺陷，它们也可能看起来是正确的 。
	 * 
	 * 有一点很重要，那就是要注意到递增程序自身也需要多个步骤，并且在递增过程中任务
	 * 可能会被线程机制挂起---也就是说，在Java中，递增不是原子性的操作。因为，如果
	 * 不保护任务，即使单一的递增也是不安全的。
	 * 
	 * @create @author Henry @date 2016-11-24
	 * @param args
	 */
	public static void main(String[] args) {
		EvenChecker.test(new EventGenerator());
	}
}
