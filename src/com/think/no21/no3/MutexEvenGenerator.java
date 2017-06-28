package com.think.no21.no3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MutexEvenGenerator添加了一个被互斥调用的锁，并使用lock()和unlock()方法
 * 在next()内部创建了临界资源。当你在使用Lock对象时，将这里所示的惯用法内部化是很重要的：
 * 紧接着对lock()的调用，你必须放置在finally子句中带有unlock()的try-finally语句中。
 * 注意，return语句必须在try子句中出现，以确保unlock()不会过早发生，从而将数据暴露给了
 * 第二个任务。
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
public class MutexEvenGenerator extends IntGenerator {
	private int currentEvenValue = 0;
	private Lock lock=new ReentrantLock();
	/**
	 * 尽管try-finally所需的代码比synchronized关键字要多，但是
	 * 这也代表了显示的Lock对象的优点之一。如果使用synchronized关键字时，
	 * 某些事物失败了，那么就会抛出一个异常。但是你没有机会去做任何清理工作，
	 * 以维护系统使其处于良好状态。有了显式的Lock对象，你就可以使用finally
	 * 子句将系统维护在正确的状态了。
	 * 
	 * 大体上，当你使用sunchronized关键字时，需要写的代码量更少，并且用户错误出现
	 * 的可能性也会降低，因为通常只有在解决特殊问题时，才使用显式的Lock对象。
	 * 例如，用synchronized关键字不能尝试着获取锁且最终获取锁会失败，或者尝试着
	 * 获取锁一段时间，然后放弃它，要实现这些，你必须使用concurrent类库。
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
