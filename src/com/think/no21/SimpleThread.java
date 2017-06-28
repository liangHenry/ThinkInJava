package com.think.no21;


public class SimpleThread extends Thread{

	private int countDown=5;
	private static int threadCount=0;
	public SimpleThread() {
		super(Integer.toString(++threadCount));
		start();
	}
	@Override
	public String toString() {
		return "#" +getName()+"("+countDown+").";
	}
	@Override
	public void run() {
		while(true){
			System.out.print(this);
			Thread.yield();
			if(--countDown==0)
				return;
		}
	}
	public static void main(String[] args) {
		for (int i = 0; i < 2; i++) 
			new SimpleThread();
	}
}
