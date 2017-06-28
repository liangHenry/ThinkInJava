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
 * 正如注释中注明的，Pair不是线程安全的，因为它的约束条件（虽然是任意的）需要两个变量要维护成相同的值。
 * 此外，如本章前面所描述的，自增加操作不是线程安全的，并且因为没哟任何方法被标记为synchronized，所以
 * 不能保证一个Pair对象在多线程程序中不会被破坏。
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
 * 你可以想象一下这种情况：某人交个你一个非线程安全的Pair类，而你需要在一个线程环境中使用它。通过创建
 * PairManager类就可一个实现这一点，PairManager类持有一个Pair对象并控制它的一切访问。注意唯一的
 * public 方法是 getPair()，它是synchronized的。对于抽象方法increment(),对于increment()的同步
 * 控制将在实现的时候进行处理。
 */
/**
 * Protect a Pair inside a thread-safe class:
 * 
 * 至于PairManager类的结构，它的一些功能在积累中实现，并且其一个或多个抽象方法在派生类中定义，
 * 这种结构在设计模式中成为模板方法。设计模式使你得以把变化封装在代码里；因此，发生变化的部分是
 * 模板方法increment()。
 * store()方法将一个Pair对象添加到了Synchronized ArrayList中，所以这个操作是线程安全的。
 * 因此，该方法不必进行防护，可以防止在PairManager2的synchronized语句块的外部。
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
 * 在PairManager1中，整个increment()方法被同步控制。
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
 * 在PairManager2中，increment()方法使用同步控制块进行同步。
 * 注意，synchronized关键字不属于方法特性签名的组成部分，所以可以在覆盖方法的时候加上去。
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
 * PairManipulator被创建用来测试两种不同类型的PairManager，其方法是在某个任务中调用increment(),
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
 * PairChecker则在另一个任务中执行。为了跟踪可以运行测试的频度，
 * PairChecker在每次成功时都递增checkCounter。
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
 * 在main()中创建了两个PairManipulator对象，并运行它们运行一段时间，
 * 之后每个PairManipulator的结果会得到展示。
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
	 * 尽管每次运行的结果可能会非常不同，但一般来说，对于PairChecker的检查频率。
	 * PairManager1.increment()不允许有PairManager2.increment()那样多。
	 * 后者采用沟通不控制块进行同步，所以对象不加锁的时间更长。这也是宁愿使用同步控制块
	 * 而不是对整个方法进行同步控制的典型原因：使得其他线程能更多地访问
	 * (在安全的情况下尽可能多)。
	 * 
	 * 运行结果：
	 * pm1: Pair: x: 17, y: 17 checkCounter = 1714855
	 * pm2: Pair: x: 22, y: 22 checkCounter = 12120719
	 * @param args
	 */
	public static void main(String[] args) {
		PairManager pman1 = new PairManager1(), pman2 = new PairManager2();
		testApproaches(pman1, pman2);
	}
}
