package com.think.no21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * �߳��׳��쳣ʵ����try catch���޷������쳣�ġ�
 * 
 * @create @author Henry @date 2016-11-23
 *
 */
public class ExceptionRun extends Thread{
	@Override
	public void run() {
		throw new RuntimeException();
	}
	public static void main(String[] args) {
		try {
			ExecutorService exec=Executors.newCachedThreadPool();
			exec.execute(new ExceptionRun());
		} catch (Exception e) {
			System.out.println();
		}
	}
}
