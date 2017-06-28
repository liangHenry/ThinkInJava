package com.think.no21.no3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**每条指令都会产生一个get和put，它们之间还有一些其他的指令。因此在获取和放置之间，
 * 另一个任务可能会修改这个域，所以，这些操作不是原子性的：
 * 如果你盲目地应用原子性概念，那么就会看到在下面程序中的getValue()符合上面的描述：
 * 该程序降到奇数值并终止。
 * 
 * 解决办法getValue()和evenIncrement()必须是synchronized的。
 * 在诸如此类情况下，只有并发专家才有能力进行优化，而你还是应该运用Brian的同步规则。
 * 
 * @create @author Henry @date 2016-11-28
 *
 */
public class AtomicityTest implements Runnable {
	/**
	 * 由于i也不是volatile的，因此还存在可视性问题。
	 */
	private int i=0;
	/**
	 * 尽管return i 确实是原子性操作，但是缺少同步使得其数值可以子在处于不稳定的中间
	 * 状态时被读取。
	 * @return
	 */
	public int getValue(){return i;}
	
	private synchronized void evenIncrement(){i++;i++;}
	
	@Override
	public void run() {
		while(true)
			evenIncrement();
	}
	public static void main(String[] args) {
		ExecutorService exec=Executors.newCachedThreadPool();
		AtomicityTest at=new AtomicityTest();
		exec.execute(at);
		while(true){
			int val=at.getValue();
			if(val%2!=0){
				System.out.println(val);
				System.exit(0);
			}
		}
	}
}
