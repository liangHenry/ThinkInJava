package com.think.no21.no6;

import java.util.Random;
import java.util.concurrent.TimeUnit;
/**
 * 哲学家
 * 
 * 在Philosopher，run()中，每个Philosopher只是不断地思考和吃饭。如果PonderFactor不为0，则pause()方法会休眠(sleep())
 * 一段随机的时间。通过使用这种方式，你将看到Philosopher会在思考上花掉一段随机化的时间，然后尝试着获取(take())右边和左边的
 * Chopstick，随后在吃饭上花掉一段随机化的时间，之后重复此过程。
 * 
 * @create @author Henry @date 2016-12-26
 *
 */
public class Philosopher implements Runnable {
	private Chopstick left;
	private Chopstick right;
	private final int id;
	private final int ponderFactor;
	private Random rand = new Random(47);

	public Philosopher(Chopstick left, Chopstick right, int id, int ponderFactor) {
		this.left = left;
		this.right = right;
		this.id = id;
		this.ponderFactor = ponderFactor;
	}

	private void pause() throws InterruptedException {
		if (ponderFactor == 0)
			return;
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println(this + " " + "thinking");
				pause();
				// Philosopher becomes hungry
				System.out.println(this + " " + "grabbing right");
				right.take();
				System.out.println(this + " " + "grabbing left");
				left.take();
				System.out.println(this + " " + "eating");
				pause();
				right.drop();
				left.drop();
			}
		} catch (InterruptedException e) {
			System.err.println(this + " " + "exiting via interrupt");
		}
	}

	@Override
	public String toString() {
		return "Philosopher " + id;
	}
}
