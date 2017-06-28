package com.think.no21.no4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * װ���Ի�԰
 * 
 * �������������У���԰ίԱ��ϣ���˽�ÿ��ͨ��������Ž��빫������������
 * ÿ�����Ŷ���һ��ʮ��ת�Ż�ĳ��������ʽ�ļ������������κ�һ��ʮ��ת��
 * �ļ���ֵ����ʱ���ͱ�ʾ��԰�е��������Ĺ������ֵҲ�������
 * 
 * @create @author Henry @date 2016-12-06
 * 
 */

/**
 * ����ʹ�õ�����Count���������ٻ�ԭ�ι��ߵ�������ֵ�����ҽ��䵱��Entrance���е�һ��
 * ��̬����д洢��Count.increment()��Count.value()����synchronized�ģ�
 * �������ƶ�count��ķ��ʡ�
 * 
 * @create @author Henry @date 2016-12-06
 */
class Count {
	private int count = 0;
	private Random rand = new Random(47);
	
	/**
	 * increment()����ʹ����Random����
	 * Ŀ���ǴӰ�count��ȡ��temp�У�������temp������洢��count�����ʱ���
	 * �д�Լһ���ʱ������ò���
	 * ����㽫increment()�ϵ�synchronized�ؼ���ע�͵�����ô�������ͻ������
	 * ��Ϊ�������ͬʱ���ʲ��޸�count(yield()��ʹ�������ط���)
	 * @create @author Henry @date 2016-12-06
	 * 
	 * @return
	 */
	// Remove the synchronized keyword to see counting fail:
	public synchronized int increment() {
		int temp = count;
		if (rand.nextBoolean())// Yield half the time
			Thread.yield();
		return (count = ++temp);
	}

	public synchronized int value() {
		return count;
	}
}
/**
 * Entrance���е���һ��Count������Ϊ��̬����д洢��
 * ÿ��Entrance����ά����һ������ֵnumber��������ͨ��ĳ���ض���ڽ���Ĳι��ߵ�
 * ���������ṩ�˶�count�����˫�ؼ�飬��ȷ�����¼�Ĳι�����������ȷ�ġ�Entrance.run()
 * ֻ�ǵ���number��count����Ȼ������100���롣
 * ��ΪEntrance.canceled��һ��volatile������־������ֻ�ᱻ��ȡ�͸�ֵ
 * �������������������һ���¶�ȡ�������Բ���Ҫͬ������ķ��ʣ��Ϳ��԰�ȫ�ز�������
 * ��������������������κ����ǣ���ô�������ʹ��synchronized��
 * 
 * @create @author Henry @date 2016-12-06
 *
 */
class Entrance implements Runnable {
	private static Count count = new Count();
	private static List<Entrance> entrances = new ArrayList<Entrance>();
	private int number = 0;
	// Doesn't need synchronization to read:
	private final int id;
	private static volatile boolean canceled = false;

	// Atomic operation on a volatile field:
	public static void cancel() {
		canceled = true;
	}

	public Entrance(int id) {
		this.id = id;
		// Keep this task in a list. Also prevents
		// garbage collection of dead tasks:
		entrances.add(this);
	}

	@Override
	public void run() {
		while (!canceled) {
			synchronized (this) {
				++number;
			}
			System.out.println(this + " Total: " + count.increment());
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("sleep interrupted");
			}
		}
		System.out.println("Stopping " + this);
	}

	public synchronized int getValue() {
		return number;
	}

	@Override
	public String toString() {
		return "Entrance " + id + ": " + getValue();
	}
	
	public static int getTotalCount(){
		return count.value();
	}
	
	public static int sumEntrances(){
		int sum=0;
		for (Entrance entrance : entrances)
			sum+=entrance.getValue();
		return sum;
	}
}
/**
 * ������������ȶ��ķ�ʽ�ر��������﷽�滹��һЩС�鷳���䲿��ԭ����Ϊ��˵������ֹ
 * ���̳߳���ʱ������൱С�ģ�����һ����ԭ����Ϊ����ʾinterrupt()ֵ���Ժ��㽫ѧϰ
 * �й����ֵ��֪ʶ��
 * 
 * ��3����֮��main()��Entrance����static cancel()��Ϣ��Ȼ�����exec�����
 * shutdown()������֮�����exec�ϵ�awaitTermination()������
 * ExecutorService.awaitTermination()�ȴ�ÿ�����������������е������ڳ�ʱʱ�䵽��
 * ֮ǰȫ���������򷵻�true,���򷵻�false,��ʾ�������е������Ѿ������ˡ�������ᵼ��
 * ÿ�������˳���run()�������������Ϊ�������ֹ������Entrance�����Ծ�����Ч�ģ�
 * ��Ϊ�ڹ������У�ÿ��Entrance���󶼴洢�ڳ�Ϊentrances�ľ�̬List<Entrance>�С�
 * ��ˣ�sumEntrances()�Ծɿ�����������Щ��Ч��Entrance����
 * 
 * �������������ʱ���㽫������������ͨ��ʮ��ת��ʱ������ʾ��������ͨ��ÿ����ڵ�������
 * ����Ƴ�Count.increment()�����synchronized�������㽫��ע�⵽����������������в��죬
 * ÿ��ʮ��ת��ͳ�Ƶ���������count�е�ֵ��ͬ��ֻҪ�û�����ͬ����Count�ķ��ʣ�����Ϳ��Խ����
 * ���ס��Count.increment()ͨ��ʹ��temp��yield()��������ʧ�ܵĿ����ԡ���������
 * �߳������У�ʧ�ܵĿ����Դ�ͳ��ѧ�Ƕȿ��ܷǳ�С���������ܺ����׾͵����������������ﶼ����ȷ
 * ����������������������ʾ���У���Щ��δ����������п��ܻ���������������ڸ��󲢷�����ʱ��
 * Ҫ�������ϸ��
 * 
 * 
 * 
 * @create @author Henry @date 2016-12-06
 */
public class OrnamentalGarden {
	public static void main(String[] args) throws Exception {
		ExecutorService exec=Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++)
			exec.execute(new Entrance(i));
		TimeUnit.SECONDS.sleep(3);
		
		Entrance.cancel();
		exec.shutdown();
		
		if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
			System.out.println("Some tasks were not teminated");
		System.out.println("Total: "+Entrance.getTotalCount());
		System.out.println("Sum of Entrances:"+Entrance.sumEntrances());
		
	}
}
