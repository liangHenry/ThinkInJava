package com.think.no21.no3.test;

import java.util.concurrent.TimeUnit;

public class MyObject {
	public synchronized void method1(){
		System.out.println("method1 is coming");
		try {
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		method3();
		System.out.println("method1 is end");
	}
	public synchronized void method2(){
		System.out.println("method2 is coming");
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		method3();
		System.out.println("method2 is end");
	}
	public  void method3(){
		System.out.println("method3 is coming");
		method1();
		System.out.println("method3 is end");
	}
}
