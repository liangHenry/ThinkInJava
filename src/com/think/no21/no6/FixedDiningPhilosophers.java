package com.think.no21.no6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 通过确保最后一个Philosopher先拿起和放下左边的Chopstick，我们可以移除死锁，从而使这个程序平滑地运行。
 * 
 * 
 * @create @author Henry @date 2016-12-26
 *
 */
public class FixedDiningPhilosophers {
	public static void main(String[] args) throws Exception {
		int ponder = 0;
		int size = 5;
		ExecutorService exec = Executors.newCachedThreadPool();
		Chopstick[] sticks = new Chopstick[size];
		for (int i = 0; i < size; i++)
			sticks[i] = new Chopstick();
		for (int i = 0; i < size; i++)
			if (i < (size - 1))
				exec.execute(new Philosopher(sticks[i], sticks[i + 1], i, ponder));
			else
				exec.execute(new Philosopher(sticks[0], sticks[i], i, ponder));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
