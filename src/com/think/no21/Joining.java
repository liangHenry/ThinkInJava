package com.think.no21;

/**
 * * join()�������ͣ�
 * һ���߳̿����������߳�֮�ϵ���Join()��������Ч���ǵȴ�һ��ʱ��ֱ���ڶ����߳̽����ż���ִ�С�
 * ���ĳ���߳�����һ���߳�t�ϵ���t.join()�����߳̽�������ֱ��Ŀ���߳�t�����Żָ�
 * ����t.isAlive()����Ϊ�٣���
 * Ҳ�����ڵ���join()ʱ����һ����ʱ��������λ�����Ǻ��룬���ߺ�������룩��
 * �������Ŀ���߳������ʱ�䵽��ʱ��û�н����Ļ���join()�������ܷ��ء�
 * ��join()�����ĵ��ÿ��Ա��жϣ�����ʵ���ڵ����߳��ϵ���interrupt()������
 * ��ʱ��Ҫ����try-catch�Ӿ䡣
 */
/**
 * Sleeper ��һ��Thread���ͣ�����Ҫ����һ��ʱ�䣬���ʱ����ͨ���������������Ĳ�����ָ���ġ�
 * ��run()�У�sleep()�����п�����ָ����ʱ������ʱ���أ���Ҳ���ܱ��жϡ�
 * ��catch�Ӿ��У�������isInterrpted()�ķ���ֵ��������жϡ�����һ���߳��ڸ��߳��ϵ���interrupt()ʱ��
 * �������߳��趨һ����־���������߳��Ѿ����жϡ�Ȼ�����쳣������ʱ�����������־��
 * ������catch�Ӿ��У����쳣�������ʱ�������־����Ϊ�١����쳣֮�⣬�����־�����������������
 * �����߳̿��ܻ������ж�״̬��
 * 
 * @create @author Henry @date 2016-11-23
 * 
 */
class Sleeper extends Thread {
	private int duration;

	public Sleeper(String name, int sleepTime) {
		super(name);
		duration = sleepTime;
		start();
	}

	@Override
	public void run() {
		try {
			sleep(duration);
			System.out.println("world");
		} catch (InterruptedException e) {
			System.out.println(getName() + " was interrupted. IsInterrupted():" + isInterrupted());
			return;
		}
		System.out.println(getName() + " has awakened");
	}
}

/**
 * Joiner �߳̽�ͨ����Sleeper�����ϵ���join()�������ȴ�Sleeper������
 * 
 * @create @author Henry @date 2016-11-23
 * 
 */
class Joiner extends Thread {
	private Sleeper sleeper;

	public Joiner(String name, Sleeper sleeper) {
		super(name);
		this.sleeper = sleeper;
		start();
	}

	@Override
	public void run() {
		try {
			System.out.println(sleeper.getName() + " join");
			sleeper.join();
		} catch (InterruptedException e) {
			System.out.println("Interrupted---");
		}
		System.out.println(getName() + " join completed");
	}
}

/**
 * ��main()���棬ÿ��Sleeper����һ��Joiner,�������������з��֣�
 * ���sleeper���жϻ�������������Joiner����Sleeperһͬ������
 * 
 * @create @author Henry @date 2016-11-23
 * 
 */
public class Joining {
	public static void main(String[] args) {
		Sleeper sleepy = new Sleeper("Sleepy", 1500), grumpy = new Sleeper("Grumpy", 1500);
		Joiner dopey = new Joiner("Dopey", sleepy), doc = new Joiner("Doc", grumpy);
		// System.out.println("hello");
		// grumpy.interrupt();
	}
}
