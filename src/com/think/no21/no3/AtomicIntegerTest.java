package com.think.no21.no3;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Java SE5����������AtomicInteger,AtomicLong,AtomicReference �������ԭ���Ա����࣬
 * �����ṩ������ʽ��ԭ�����������²�����
 * boolean compareAndSet(expectedValue,updateValue);
 * ��Щ�౻����Ϊʹ����ĳЩ�ִ��������ϵĿɻ�õģ��������ڻ��������ϵ�ԭ���ԣ������ʹ������ʱ��
 * ͨ������Ҫ���ġ����ڳ�������˵�����Ǻ��ٻ������ó����������漰���ܵ���ʱ�����Ǿʹ�������֮���ˡ�
 * 
 * 
 * @create @author Henry @date 2016-11-30
 *
 */
public class AtomicIntegerTest implements Runnable{
	private AtomicInteger i=new AtomicInteger(0);
	public int getValue(){return i.get();}
	private void evenIncrement(){i.addAndGet(2);}
	@Override
	public void run() {
		while(true)
			evenIncrement();
	}
	/**
	 * ����ͨ��ʹ��AtomicInteger��������synchronized�ؼ��֡���Ϊ������򲻻�ʧ�ܣ�
	 * ���Լ���һ��Timer���Ա���5����֮���Զ�����ֹ��
	 * 
	 * @create @author Henry @date 2016-11-30
	 * @param args
	 */
	public static void main(String[] args) {
		/*new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				System.err.println("Aborting");
				System.exit(0);
			}
		}, 5000);*/
		ExecutorService exec=Executors.newCachedThreadPool();
		AtomicIntegerTest ait=new AtomicIntegerTest();
		exec.execute(ait);
		while(true){
			int val=ait.getValue();
			if(val%2!=0){
				System.out.println(val);
				System.exit(0);
			}
		}
	}
}
