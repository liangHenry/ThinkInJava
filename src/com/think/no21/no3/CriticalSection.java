package com.think.no21.no3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Not thread-safe
 * ����ע����ע���ģ�Pair�����̰߳�ȫ�ģ���Ϊ����Լ����������Ȼ������ģ���Ҫ��������Ҫά������ͬ��ֵ��
 * ���⣬�籾��ǰ���������ģ������Ӳ��������̰߳�ȫ�ģ�������ΪûӴ�κη��������Ϊsynchronized������
 * ���ܱ�֤һ��Pair�����ڶ��̳߳����в��ᱻ�ƻ���
 * 
 * @create @author Henry @date 2016-11-30
 */
class Pair {
	private int x, y;

	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Pair() {
		this(0, 0);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void incrementX() {
		x++;
	}

	public void incrementY() {
		y++;
	}

	@Override
	public String toString() {
		return "x: " + x + ", y: " + y;
	}

	public class PairValuesNotEqualExceptin extends RuntimeException {
		public PairValuesNotEqualExceptin() {
			super("Pair values not equal : " + Pair.this);
		}
	}

	/**
	 * Arbitrary invariant --both variables must be equal:
	 * 
	 * @create @author Henry @date 2016-11-30
	 */
	public void checkState() {
		if (x != y)
			throw new PairValuesNotEqualExceptin();
	}
}
/**
 * ���������һ�����������ĳ�˽�����һ�����̰߳�ȫ��Pair�࣬������Ҫ��һ���̻߳�����ʹ������ͨ������
 * PairManager��Ϳ�һ��ʵ����һ�㣬PairManager�����һ��Pair���󲢿�������һ�з��ʡ�ע��Ψһ��
 * public ������ getPair()������synchronized�ġ����ڳ��󷽷�increment(),����increment()��ͬ��
 * ���ƽ���ʵ�ֵ�ʱ����д���
 */
/**
 * Protect a Pair inside a thread-safe class:
 * 
 * ����PairManager��Ľṹ������һЩ�����ڻ�����ʵ�֣�������һ���������󷽷����������ж��壬
 * ���ֽṹ�����ģʽ�г�Ϊģ�巽�������ģʽʹ����԰ѱ仯��װ�ڴ������ˣ������仯�Ĳ�����
 * ģ�巽��increment()��
 * store()������һ��Pair������ӵ���Synchronized ArrayList�У���������������̰߳�ȫ�ġ�
 * ��ˣ��÷������ؽ��з��������Է�ֹ��PairManager2��synchronized������ⲿ��
 * 
 * @create @author Henry @date 2016-11-30
 */
abstract class PairManager {
	AtomicInteger checkCounter = new AtomicInteger(0);
	protected Pair p = new Pair();
	private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());

	/**
	 * Make a copy to keep the original safe
	 * 
	 * @create @author Henry @date 2016-11-30
	 * @return
	 */
	public synchronized Pair getPair() {
		return new Pair(p.getX(), p.getY());
	}

	protected void store(Pair p) {
		storage.add(p);
		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} catch (InterruptedException e) {
		}
	}

	public abstract void increment();
}

/**
 * Synchronized the entire method:
 * 
 * ��PairManager1�У�����increment()������ͬ�����ơ�
 * 
 * @create @author Henry @date 2016-11-30
 * 
 */
class PairManager1 extends PairManager {

	@Override
	public synchronized void increment() {
		p.incrementX();
		p.incrementY();
		store(getPair());
	}
}

/**
 * Use a critical section:
 * 
 * ��PairManager2�У�increment()����ʹ��ͬ�����ƿ����ͬ����
 * ע�⣬synchronized�ؼ��ֲ����ڷ�������ǩ������ɲ��֣����Կ����ڸ��Ƿ�����ʱ�����ȥ��
 * 
 * @create @author Henry @date 2016-11-30
 * 
 */
class PairManager2 extends PairManager {
	@Override
	public void increment() {
		Pair temp;
		synchronized (this) {
			p.incrementX();
			p.incrementY();
			temp = getPair();
		}
		store(temp);
	}
}
/**
 * PairManipulator�����������������ֲ�ͬ���͵�PairManager���䷽������ĳ�������е���increment(),
 * 
 * @create @author Henry @date 2016-11-30
 *
 */
class PairManipulator implements Runnable {
	private PairManager pm;

	public PairManipulator(PairManager pm) {
		this.pm = pm;
	}

	@Override
	public void run() {
		while (true)
			pm.increment();
	}

	@Override
	public String toString() {
		return "Pair: " + pm.getPair() + " checkCounter = " + pm.checkCounter.get();
	}
}
/**
 * PairChecker������һ��������ִ�С�Ϊ�˸��ٿ������в��Ե�Ƶ�ȣ�
 * PairChecker��ÿ�γɹ�ʱ������checkCounter��
 * 
 * @create @author Henry @date 2016-11-30
 *
 */
class PairChecker implements Runnable {
	private PairManager pm;

	public PairChecker(PairManager pm) {
		this.pm = pm;
	}

	@Override
	public void run() {
		while (true) {
			pm.checkCounter.incrementAndGet();
			pm.getPair().checkState();
		}
	}
}

/**
 * Test the two different approaches;
 * ��main()�д���������PairManipulator���󣬲�������������һ��ʱ�䣬
 * ֮��ÿ��PairManipulator�Ľ����õ�չʾ��
 * 
 * @create @author Henry @date 2016-11-30
 * 
 */
public class CriticalSection {
	static void testApproaches(PairManager pman1, PairManager pman2) {
		ExecutorService exec = Executors.newCachedThreadPool();
		PairManipulator pm1 = new PairManipulator(pman1), pm2 = new PairManipulator(pman2);
		PairChecker pcheck1 = new PairChecker(pman1), pcheck2 = new PairChecker(pman2);
		exec.execute(pm1);
		exec.execute(pm2);
		exec.execute(pcheck1);
		exec.execute(pcheck2);
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Sleep interrupted");
		}
		System.out.println("pm1: " + pm1 + "\npm2: " + pm2);
		System.exit(0);
	}
	/**
	 * ����ÿ�����еĽ�����ܻ�ǳ���ͬ����һ����˵������PairChecker�ļ��Ƶ�ʡ�
	 * PairManager1.increment()��������PairManager2.increment()�����ࡣ
	 * ���߲��ù�ͨ�����ƿ����ͬ�������Զ��󲻼�����ʱ���������Ҳ����Ըʹ��ͬ�����ƿ�
	 * �����Ƕ�������������ͬ�����Ƶĵ���ԭ��ʹ�������߳��ܸ���ط���
	 * (�ڰ�ȫ������¾����ܶ�)��
	 * 
	 * ���н����
	 * pm1: Pair: x: 17, y: 17 checkCounter = 1714855
	 * pm2: Pair: x: 22, y: 22 checkCounter = 12120719
	 * @param args
	 */
	public static void main(String[] args) {
		PairManager pman1 = new PairManager1(), pman2 = new PairManager2();
		testApproaches(pman1, pman2);
	}
}
