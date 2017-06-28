package com.think.no21;

import java.util.concurrent.TimeUnit;
/**
 * 如果是一个后台线程，那么它创建的任何线程江北自动设置成后台线程，
 * 例子如下。
 * @create @author Henry @date 2016-11-18
 *
 */

public class Daemons {
	/**
	 * 运行结果如下：
	 * d.isDaemon = true.
	 * DaemonSpawn 0 started.
	 * DaemonSpawn 1 started.
	 * DaemonSpawn 2 started.
	 * DaemonSpawn 3 started.
	 * DaemonSpawn 4 started.
	 * DaemonSpawn 5 started.
	 * DaemonSpawn 6 started.
	 * DaemonSpawn 7 started.
	 * DaemonSpawn 8 started.
	 * DaemonSpawn 9 started.
	 * t[0].isDaemon()=true
	 * t[1].isDaemon()=true
	 * t[2].isDaemon()=true
	 * t[3].isDaemon()=true
	 * t[4].isDaemon()=true
	 * t[5].isDaemon()=true
	 * t[6].isDaemon()=true
	 * t[7].isDaemon()=true
	 * t[8].isDaemon()=true
	 * t[9].isDaemon()=true
	 *
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		Thread d =new Thread(new Daemon());
		d.setDaemon(true);
		d.start();
		System.out.println("d.isDaemon = "+d.isDaemon()+".");
		TimeUnit.SECONDS.sleep(1);
	}
}
/**
 * 在主线程下创建的线程中包含此子线程。
 * @create @author Henry @date 2016-11-18
 *
 */
class Daemon implements Runnable {
	private Thread[] t = new Thread[10];

	@Override
	public void run() {
		for (int i = 0; i < t.length; i++) {
			t[i] = new Thread(new DaemonSpawn());
			t[i].start();
			System.out.println("DaemonSpawn " + i + " started.");
		}
		for (int i = 0; i < t.length; i++)
			System.out.println("t[" + i + "].isDaemon()=" + t[i].isDaemon());
		while (true)
			Thread.yield();
	}
}
/**
 * 
 * 在子线程内再创建的子线程。
 * @create @author Henry @date 2016-11-18
 */
class DaemonSpawn implements Runnable {
	@Override
	public void run() {
		while (true) {
			Thread.yield();
		}
	}
}