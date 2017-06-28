package com.think.no21.no3.test;

public class MyRunnable1 {
	private MyObject myObject;
	private Thread t;
	
	public MyRunnable1(MyObject myObject) {
		this.myObject = myObject;
	}

	public void runTask(){
		if(t==null){
			t=new Thread(){
				@Override
				public void run() {
					System.out.println("MyRunnable1 "+Thread.currentThread());
					myObject.method1();
				}
			};
		}
		t.start();
	}
	
}
