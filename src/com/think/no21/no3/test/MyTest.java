package com.think.no21.no3.test;

public class MyTest {
	public static void main(String[] args) {
		MyObject o=new MyObject();
		MyRunnable1 myRunnable1 =new MyRunnable1(o);
		MyRunnable2 myRunnable2 =new MyRunnable2(o);
		myRunnable1.runTask();
		myRunnable2.runTask();
		System.out.println("main ending");
	}
}
