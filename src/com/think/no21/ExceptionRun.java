package com.think.no21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程抛出异常实例。try catch是无法捕获异常的。
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
