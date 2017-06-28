package com.think.no21.no3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 在本例中可以被撤销的类不是Runnable，而所有依赖于IntGenerator对象的EvenChecker任务
 * 将测试它，以查看它是否已经被撤销，正如你在run()中所见。通过这种方式，共享公共资源（IntGenerator）
 * 的任务可以观察该资源的终止信号。这可以消除所谓竞争条件，即两个或更多的任务竞争响应某个条件，
 * 因此产生冲突或不一致结果的情况。
 * 你必须仔细考虑并防范并发系统失败的所有可能途径，
 * 例如，一个任务不能依赖于另一个任务，因为任务关闭的顺序无法得到保证。
 * 这里通过使任务依赖于非任务对象，我们可以消除潜在的竞争条件。
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
public class EvenChecker implements Runnable{
	private IntGenerator generator;
	private final int id;
	
	public EvenChecker(IntGenerator generator, int id) {
		this.generator = generator;
		this.id = id;
	}
	/**
	 * EvenChecker任务总是读取和测试从与其相关的IntGenerator返回的值。注意，
	 * 如果generator.isCanceled()为true，则run()将返回，这将告知EvenChecker.test()
	 * 中的Executor该任务完成了。任何EvenChecker任务都可以在与其相关联的IntGenerator
	 * 上调用cancel()，这将导致所有其他使用该IntGenerator的EvenChecker得体地关闭。
	 * 在后面个节中，你将看到Java包含的用于线程终止的各种更通用的机制。
	 * 
	 * @create @author Henry @date 2016-11-24
	 */
	@Override
	public void run() {
		while(!generator.isCanceled()){
			int val=generator.next();
			if(val%2!=0){
				System.out.println(val+" not even!");
				generator.cancel();//Cancels all EvenCheckers
			}
		}
	}
	/**
	 * test() 方法通过启动大量使用相同的IntGenerator的EvenChecker,设置并执行对任何类型的
	 * IntGenerator的测试。如果IntGenerator引发失败，那么test()将报告它并返回，
	 * 否则,你必须按下Control-C来终止它。
	 * 
	 * @create @author Henry @date 2016-11-24
	 * @param gp
	 * @param count
	 */
	public static void test(IntGenerator gp,int count){
		System.out.println("Press Control-C to exit");
		ExecutorService exec=Executors.newCachedThreadPool();
		for (int i = 0; i < count; i++) 
			exec.execute(new EvenChecker(gp, i));
		exec.shutdown();
	}
	public static void test(IntGenerator gp){
		test(gp, 10);
	}
}
