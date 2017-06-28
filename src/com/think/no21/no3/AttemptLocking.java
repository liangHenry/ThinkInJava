package com.think.no21.no3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock �����㳢���Ż�ȡ������δ��ȡ������������������Ѿ���ȡ�������������
 * �Ϳ��Ծ����뿪ȥִ������һЩ���飬�����ǵȴ�ֱ����������ͷţ�������untimed()������
 * �������ġ���timed()�У������˳���ȥ���Ի�ȡ�����ó��Կ���������֮��ʧ�ܣ�ע�⣬ʹ��
 * ��java se5 ��TimeUnit����ָ��ʱ�䵥λ������main�У���Ϊ�������������һ����λ��Thread��
 * ������ȡ������ʹ��untimed()��timed()������ĳЩ���ｫ����������
 * 
 * ��ʾ��lock�����ڼ������ͷ������棬������ڽ���synchronized����˵�������������ϸ���ȵ�
 * �������������ʵ��ר��ͬ���ṹ�Ǻ����õģ��������ڱ��������б��еĽڵ㣬�ڽڴ��ݵļ�������
 * ��Ҳ��Ϊ����ϣ������ֱ�������������ͷŵ�ǰ�ڵ����֮ǰ������һ���ڵ�����
 * 
 * @create @author Henry @date 2016-11-28
 *
 */
public class AttemptLocking {
	private ReentrantLock lock = new ReentrantLock();

	public void untimed() {
		boolean captured = lock.tryLock();
		try {
			System.out.println("tryLock():" + captured);
		} finally {
			if (captured)
				lock.unlock();
			System.out.println("untimed over");
		}
	}

	public void timed() {
		boolean captured = false;
		try {
			captured = lock.tryLock(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			System.out.println("tryLock(2, TimeUnit.SECONDS):" + captured);
		} finally {
			if (captured)
				lock.unlock();
			System.out.println("timed over");
		}
	}

	public static void main(String[] args) {
		final AttemptLocking al = new AttemptLocking();
		al.untimed();// True --lock is available
		al.timed();// True -- lock is available
		// Now create a separate task to grab the lock;
		new Thread() {
			{
				//setDaemon(true);
			}
			public void run(){
				al.lock.lock();
				System.out.println("acquired");
			}
		}.start();
		Thread.yield(); //Give the 2nd tesk a chance
		al.untimed();	//False --lock grabbed by task
		al.timed();	//False --Lock grabbed by task
	}
}
