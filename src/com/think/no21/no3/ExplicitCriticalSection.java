package com.think.no21.no3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Synchronize the entire method:
 * 
 * @create @author Henry @date 2016-11-30
 *
 */
class ExplicitPairManager1 extends PairManager {
	private Lock lock = new ReentrantLock();

	@Override
	public synchronized void increment() {
		lock.lock();
		try {
			p.incrementX();
			p.incrementY();
			store(getPair());
		} finally {
			lock.unlock();
		}
	}
}
/**
 * Use a critical section
 * 
 * @create @author Henry @date 2016-11-30
 *
 */
class ExplicitPairManager2 extends PairManager {
	private Lock lock = new ReentrantLock();

	@Override
	public void increment() {
		Pair temp;
		lock.lock();
		try {
			p.incrementX();
			p.incrementY();
			temp = getPair();
		} finally {
			lock.unlock();
		}
		store(temp);
	}
}
/**
 * 此方式在我尝试的时候，直接出现线程不安全的异常。
 * 但是结果是对的。说明lock锁的是代码线程安全，而不会锁对象同步安全。 
 * 
 * 运行结果：
 * Exception in thread "pool-1-thread-4" com.think.no21.no3.Pair$PairValuesNotEqualExceptin: Pair values not equal : x: 3, y: 2
 * 	at com.think.no21.no3.Pair.checkState(CriticalSection.java:65)
 * 	at com.think.no21.no3.PairChecker.run(CriticalSection.java:192)
 * 	at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)
 * 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)
 * 	at java.lang.Thread.run(Thread.java:662)
 * pm1: Pair: x: 21, y: 21 checkCounter = 255040
 * pm2: Pair: x: 22, y: 22 checkCounter = 1884633
 * 
 * @create @author Henry @date 2016-11-30
 *
 */
public class ExplicitCriticalSection {
	public static void main(String[] args) {
		PairManager pman1 = new ExplicitPairManager1(), pman2 = new ExplicitPairManager2();
		CriticalSection.testApproaches(pman1, pman2);
	}
}
