package com.think.no21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * ��Ӧ����ʶ����̨�����ڲ�ִ��finally�Ӿ������¾ͻ���ֹ��run()����.
 * ������˵����������һ����̨�߳̽�����ʱ�������ĺ�̨�߳��ǲ�����ֹ�ġ�
 * @create @author Henry @date 2016-11-18
 *
 */
class ADeamon implements Runnable {
	private static int taskCount = 0;
	private final int id = taskCount++;
	@Override
	public void run() {
		try {
			System.out.println(id+" Starting ADaemon");
			if(id!=1){
				TimeUnit.SECONDS.sleep(3);
			}
		} catch (InterruptedException e) {
			System.out.println("Exiting via InterruptedExecption");
		}finally{
			System.out.println(id+" This should always run?");
		}
	}
}
/**
 * ���������������ʱ���㽫����finally�Ӿ�Ͳ���ִ�У�
 * ���������ע�͵���setDaemon()�ĵ��ã��ͻῴ��finally�Ӿ佫��ִ�С�
 * 
 * @create @author Henry @date 2016-11-18
 *
 */
public class DaemonsDontRunFinally {
	/**
	 * 
	 * @create @author Henry @date 2016-11-18
	 * ���н�����£�
	 * Starting ADaemon
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 3; i++) {
//			Thread t=new Thread(new ADeamon());
//			t.setDaemon(true);
//			t.start();
//		}
		ExecutorService exec=Executors.newCachedThreadPool();
		for (int i = 0; i < 3; i++)
			exec.execute(new ADeamon());
		TimeUnit.SECONDS.sleep(2);
		exec.shutdown();
		TimeUnit.SECONDS.sleep(10);
		System.out.println("hello");
	}
}
