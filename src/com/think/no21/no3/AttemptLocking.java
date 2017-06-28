package com.think.no21.no3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 允许你尝试着获取单最终未获取锁，这样如果其他人已经获取了这个锁，那你
 * 就可以决定离开去执行其他一些事情，而不是等待直到这个锁被释放，就像在untimed()方法中
 * 所看到的。在timed()中，作出了尝试去尝试获取锁，该尝试可以在两秒之后失败（注意，使用
 * 了java se5 的TimeUnit类来指定时间单位）。在main中，作为匿名类而创建了一个单位的Thread，
 * 它将获取锁，这使得untimed()和timed()方法对某些事物将产生竞争。
 * 
 * 显示的lock对象在枷锁和释放锁方面，相对于内建的synchronized锁来说，还赋予了你更细粒度的
 * 控制力。这对于实现专有同步结构是很有用的，例如用于遍历链接列表中的节点，节节传递的枷锁机制
 * （也称为锁耦合），这种遍历代码必须在释放当前节点的锁之前不活下一个节点锁。
 * 
 * @create @author Henry @date 2016-11-28
 *
 */
public class AttemptLocking {
	private ReentrantLock lock = new ReentrantLock();

	public void untimed() {
		boolean captured = lock.tryLock();
		try {
			System.out.println("tryLock():" + captured);
		} finally {
			if (captured)
				lock.unlock();
			System.out.println("untimed over");
		}
	}

	public void timed() {
		boolean captured = false;
		try {
			captured = lock.tryLock(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			System.out.println("tryLock(2, TimeUnit.SECONDS):" + captured);
		} finally {
			if (captured)
				lock.unlock();
			System.out.println("timed over");
		}
	}

	public static void main(String[] args) {
		final AttemptLocking al = new AttemptLocking();
		al.untimed();// True --lock is available
		al.timed();// True -- lock is available
		// Now create a separate task to grab the lock;
		new Thread() {
			{
				//setDaemon(true);
			}
			public void run(){
				al.lock.lock();
				System.out.println("acquired");
			}
		}.start();
		Thread.yield(); //Give the 2nd tesk a chance
		al.untimed();	//False --lock grabbed by task
		al.timed();	//False --Lock grabbed by task
	}
}
