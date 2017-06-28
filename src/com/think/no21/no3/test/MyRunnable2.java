package com.think.no21.no3.test;

public class MyRunnable2 {
	private MyObject myObject;
	private Thread t;
	public MyRunnable2(MyObject myObject) {
		this.myObject = myObject;
	}
	
	public void setMyObject(MyObject myObject) {
		this.myObject = myObject;
	}
	public void runTask(){
		if(t==null){
			t=new Thread(){
				@Override
				public void run() {
					System.out.println("MyRunnable2 "+Thread.currentThread());
					myObject.method2();
				}
			};
		}
		t.start();
	}
}