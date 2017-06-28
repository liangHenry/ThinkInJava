package com.think.no21.no6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * �������ǿ��Խ����������Ľ�����������İ汾�ˣ�
 * 
 * ��ᷢ�֣����Philosopher����˼���ϵ�ʱ��ǳ��٣���ô��������Ҫ����ʱ��ȫ������Chopstick�ϲ���������������Ҳ�͸���ط�����
 * 
 * @create @author Henry @date 2016-12-26
 *
 */

public class DeadLockingDiningPhilosophers {
	public static void main(String[] args) throws Exception {
		int ponder=0;
		int size=5;
		ExecutorService exec=Executors.newCachedThreadPool();
		Chopstick[] sticks=new Chopstick[size];
		for(int i=0;i<size;i++)
			sticks[i]=new Chopstick();
		for(int i=0;i<size;i++)
			exec.execute(new Philosopher(sticks[i], sticks[(i+1)%size], i, ponder));
		TimeUnit.SECONDS.sleep(3);
		exec.shutdownNow();
	}
}
