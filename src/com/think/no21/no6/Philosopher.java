package com.think.no21.no6;

import java.util.Random;
import java.util.concurrent.TimeUnit;
/**
 * ��ѧ��
 * 
 * ��Philosopher��run()�У�ÿ��Philosopherֻ�ǲ��ϵ�˼���ͳԷ������PonderFactor��Ϊ0����pause()����������(sleep())
 * һ�������ʱ�䡣ͨ��ʹ�����ַ�ʽ���㽫����Philosopher����˼���ϻ���һ���������ʱ�䣬Ȼ�����Ż�ȡ(take())�ұߺ���ߵ�
 * Chopstick������ڳԷ��ϻ���һ���������ʱ�䣬֮���ظ��˹��̡�
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
