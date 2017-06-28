package com.think.no21;

public class Test221 {
	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			new Thread(new MyRun()).start();
		}
	}
}

class MyRun implements Runnable{
	protected int countDown=3;
	private static int taskCount=0;
	private final int id=taskCount++;
	public MyRun() {
		System.out.println("id:"+id+" I will run");
	}
	@Override
	public void run() {
		while(countDown-->0){
			printInfo();
			Thread.yield();
		}
		System.out.println("id:"+id+" I will end");
		return ;
	}
	private void printInfo(){
		System.out.println("id:"+id+" countDown:"+countDown);
	}
}