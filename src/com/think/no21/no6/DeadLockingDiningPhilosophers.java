package com.think.no21.no6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 现在我们可以建立这个程序的将会产生死锁的版本了：
 * 
 * 你会发现，如果Philosopher花在思考上的时间非常少，那么当他们想要进餐时，全部会在Chopstick上产生竞争，而死锁也就更快地发生。
 * 
 * @create @author Henry @date 2016-12-26
 *
 */

public class DeadLockingDiningPhilosophers {
	public static void main(String[] args) throws Exception {
		int ponder=0;
		int size=5;
		ExecutorService exec=Executors.newCachedThreadPool();
		Chopstick[] sticks=new Chopstick[size];
		for(int i=0;i<size;i++)
			sticks[i]=new Chopstick();
		for(int i=0;i<size;i++)
			exec.execute(new Philosopher(sticks[i], sticks[(i+1)%size], i, ponder));
		TimeUnit.SECONDS.sleep(3);
		exec.shutdownNow();
	}
}
