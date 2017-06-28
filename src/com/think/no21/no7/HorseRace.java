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
 * CyclicBarrier�������������������ϣ������һ���������ǲ��е�ִ�й�����Ȼ���ڽ�����һ������֮ǰ�ȴ���
 * ֱ������������ɣ���������Щ��join()������ʹ�����еĲ������񶼽���դ�����жӣ���˿���һֱ����ǰ�ƶ���
 * ��ǳ���CountDownLatch��ֻ��CountDownLatch��ֻ����һ�ε��¼�����CyclicBarrier���Զ�����á�
 * 
 * ����ʧ�ܵ�ͬ�����ԣ�CyclicBarrier ʹ����һ��Ҫôȫ��Ҫôȫ�� (all-or-none) ���ƻ�ģʽ��
 * �����Ϊ�жϡ�ʧ�ܻ��߳�ʱ��ԭ�򣬵����̹߳�����뿪�����ϵ㣬��ô�ڸ����ϵ�ȴ������������߳�Ҳ��ͨ�� BrokenBarrierException
 * ��������Ǽ���ͬʱ���жϣ����� InterruptedException���Է����ķ�ʽ�뿪�� 
 * �ڴ�һ����Ч�����߳��е��� await() ֮ǰ�Ĳ��� happen-before ��Щ�����ϲ�����һ���ݵĲ�����
 * �������� happen-before �����ڴ���һ���߳��ж�Ӧ await() �ɹ����صĲ����� 
 * 
 * ������Hosrac��������Ϸ���������Ķ��̰߳汾������ʹ����CyclicBarrier
 * 
 */
/**
 * ��Ķ���ÿƥ�����Լ��ı�ţ�ÿ��ǰ��������һ��������Ĳ�����ǰ��һ�κ���еȴ���
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
	 * ������ܵĲ�����
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
 * ���ܵ������峤��Ϊ75��
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
