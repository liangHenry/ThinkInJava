package com.think.no21.no6;
/**
 * ����
 * 
 * �κ�������ѧ�ң�Philosopher�������ܳɹ�take()ͬһ�����ӡ����⣬���һ�����ӣ�Chopstick���Ѿ���ĳ����ѧ�ң�Philosopher)��ã�
 * ��ô��һ��Philosopher����wait()��ֱ�����Chopstick�ĵ�ǰ�����ߵ���drop()ʹ�����Ϊֹ��
 * 
 * ��һ��Philosopher�������take()ʱ�����philosopher���ȴ���ֱ��taken��־Ϊfalse��ֱ����ǰ����Chopstick��Philosopher�ͷ�������
 * Ȼ���������Ὣtaken��־����Ϊtrue,�Ա�ʾ�������µ�Philosopher�������Chopstick�������Philosopherʹ�������Chopstickʱ��
 * �������drop()���޸ı�־��״̬����notifyAll()����������Philosopher����ЩPhilosopher����Щ���ܾ���wait()���Chopstick��
 * 
 * @create @author Henry @date 2016-12-26
 *
 */
public class Chopstick {
	private boolean taken = false;

	public synchronized void take() throws InterruptedException {
		while (taken)
			wait();
		taken = true;
	}

	public synchronized void drop() {
		taken = false;
		notifyAll();
	}
}
