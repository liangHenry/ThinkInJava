package com.think.no21.no5;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 吐司BlockingQueue
 * 
 * 考虑下面这个使用BlockingQueue的示例，有一台机器具有三个任务：一个制作吐司、一个给吐司抹黄油，
 * 另一个在抹过黄油的吐司上涂果酱。我们可以通过各个处理过程之间的BlockingQueue来运行这个吐司制作程序：
 * 
 * @create @author Henry @date 2016-12-23
 * 
 */
class Toast {
	public enum Status {
		DRY, BUTTERED, JAMMED
	}

	private Status status = Status.DRY;
	private final int id;

	public Toast(int idn) {
		id = idn;
	}

	public void butter() {
		status = Status.BUTTERED;
	}

	public void jam() {
		status = Status.JAMMED;
	}

	public Status getStatus() {
		return status;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Toast " + id + ": " + status;
	}
}

class ToastQueue extends LinkedBlockingQueue<Toast> {
}

class Toaster implements Runnable {
	private ToastQueue toashtQueue;
	private int count = 0;
	private Random rand = new Random(47);

	public Toaster(ToastQueue tq) {
		toashtQueue = tq;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
				Toast t = new Toast(count++);
				System.out.println(t);
				toashtQueue.put(t);
			}
		} catch (InterruptedException e) {
			System.err.println("Toaster interrupted");
		}
		System.out.println("Toaster off");
	}
}

// Apply butter to toast;
class Butterer implements Runnable {
	private ToastQueue dryQueue, butteredQueue;

	public Butterer(ToastQueue dry, ToastQueue buttered) {
		dryQueue = dry;
		butteredQueue = buttered;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// Blocks until next piece of toast is availble;
				Toast t = dryQueue.take();
				t.butter();
				System.out.println(t);
				butteredQueue.put(t);
			}
		} catch (InterruptedException e) {
			System.err.println("Butterer interrupted");
		}
		System.out.println("Butterer off");
	}

}

// Apply jam to buttered toast;
class Jammer implements Runnable {
	private ToastQueue butteredQueue, finishedQueue;

	public Jammer(ToastQueue buttered, ToastQueue finished) {
		butteredQueue = buttered;
		finishedQueue = finished;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// Blocks until next piece of toast is available;
				Toast t = butteredQueue.take();
				t.jam();
				System.out.println(t);
				finishedQueue.put(t);
			}
		} catch (InterruptedException e) {
			System.err.println("Jammer Interrupted");
		}
		System.out.println("Jammer off");
	}
}

class Eater implements Runnable {
	private ToastQueue finishedQueue;
	private int counter = 0;

	public Eater(ToastQueue finished) {
		finishedQueue = finished;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// Blocks untill next piece of toast is available;
				Toast t = finishedQueue.take();
				// Verify that the toast is coming in order.
				// and that all pieces are getting jammed;
				if (t.getId() != counter++ || t.getStatus() != Toast.Status.JAMMED) {
					System.out.println(">>> Error: " + t);
					System.exit(1);
				} else
					System.out.println("Chomp! " + t);
			}
		} catch (InterruptedException e) {
			System.err.println("Eater interrupted");
		}
		System.out.println("Eater off");
	}

}
/**
 * Toast 是一个使用enum值的优秀示例。注意，这个示例中没有任何显示的同步(即使用Lock对象，或synchronized 关键字的同步），
 * 因为同步队列（其内部是同步的）和系统的设计隐式地管理了---每片Toast在任何时刻都由一个任务在操作。因为队列的阻塞，使得
 * 处理过程将被自动地挂起和恢复。你可以看到由BlockingQueue产生的简化十分明显。在使用显式的wait()和notifyAll()时存在
 * 的类和类之间的耦合被消除了，因为每个类都只和它的BlockingQueue通信。
 * 
 * @create @author Henry @date 2016-12-23
 *
 */
public class ToastOMatic {
	public static void main(String[] args) throws Exception {
		ToastQueue dryQueue = new ToastQueue(), butteredQueue = new ToastQueue(), finishedQueue = new ToastQueue();
		ExecutorService exec=Executors.newCachedThreadPool();
		exec.execute(new Toaster(dryQueue));
		exec.execute(new Butterer(dryQueue,butteredQueue));
		exec.execute(new Jammer(butteredQueue,finishedQueue));
		exec.execute(new Eater(finishedQueue));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
		
	}
}
