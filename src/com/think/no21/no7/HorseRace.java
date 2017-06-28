package com.think.no21.no7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CyclicBarrier
 * 
 * CyclicBarrier适用于这样的情况：你希望创建一组任务，它们并行地执行工作，然后在进行下一个步骤之前等待，
 * 直到所有任务都完成（看起来有些像join()）。它使得所有的并行任务都将在栅栏处列队，因此可以一直地向前移动。
 * 这非常像CountDownLatch，只是CountDownLatch是只触发一次的事件，而CyclicBarrier可以多次重用。
 * 
 * 对于失败的同步尝试，CyclicBarrier 使用了一种要么全部要么全不 (all-or-none) 的破坏模式：
 * 如果因为中断、失败或者超时等原因，导致线程过早地离开了屏障点，那么在该屏障点等待的其他所有线程也将通过 BrokenBarrierException
 * （如果它们几乎同时被中断，则用 InterruptedException）以反常的方式离开。 
 * 内存一致性效果：线程中调用 await() 之前的操作 happen-before 那些是屏障操作的一部份的操作，
 * 后者依次 happen-before 紧跟在从另一个线程中对应 await() 成功返回的操作。 
 * 
 * 下面是Hosrac的赛马游戏的面向对象的多线程版本，其中使用了CyclicBarrier
 * 
 */
/**
 * 马的对象，每匹马有自己的编号，每次前进将产生一个随机数的步数，前进一次后进行等待。
 * 
 * @create @author Henry @date 2017-1-3
 * 
 */
class Horse implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private int strides = 0;
	private static Random rand = new Random(47);
	private static CyclicBarrier barrier;

	public Horse(CyclicBarrier barrier) {
		this.barrier = barrier;
	}

	public synchronized int getStrides() {
		return strides;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					strides += rand.nextInt(3);
					//if(getStrides()==14)
						//throw new Exception("world");
				}
				barrier.await();
			}
		} catch (InterruptedException e) {
			System.err.println("InterruptedException");
		} catch (BrokenBarrierException e) {
			System.err.println("BrokenBarrierException");
			throw new RuntimeException(e);
		} catch (Exception e) {
			System.err.println("Exception");
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Horse " + id + " ";
	}

	/**
	 * 输出马跑的步数。
	 * 
	 * @return
	 */
	public String tracks() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < getStrides(); i++)
			s.append("*");
		s.append(id);
		return s.toString();
	}
}

/**
 * 马场跑道，定义长度为75。
 * 
 * @create @author Henry @date 2017-1-3
 * 
 */
public class HorseRace {
	static final int FINISH_LINE = 75;
	private List<Horse> horses = new ArrayList<Horse>();
	private ExecutorService exec = Executors.newCachedThreadPool();
	private CyclicBarrier barrier;

	public HorseRace(int nHorses, final int pause) {
		barrier = new CyclicBarrier(nHorses, new Runnable() {

			@Override
			public void run() {
				StringBuilder s = new StringBuilder();
				for (int i = 0; i < FINISH_LINE; i++)
					s.append("=");
				System.out.println(s.toString());
				for (Horse horse : horses)
					System.out.println(horse.tracks());
				for (Horse horse : horses)
					if (horse.getStrides() >= FINISH_LINE) {
						System.out.println(horse + " won!");
						exec.shutdownNow();
						return;
					}
				try {
					TimeUnit.MILLISECONDS.sleep(pause);
				} catch (InterruptedException e) {
					System.err.println("barrier-action sleep interrupted");
				}
			}
		});
		for (int i = 0; i < nHorses; i++) {
			Horse horse = new Horse(barrier);
			horses.add(horse);
			exec.execute(horse);
		}
	}

	public static void main(String[] args) {
		int nHorses = 8;
		int pause = 200;
		new HorseRace(nHorses, pause);
	}
}
