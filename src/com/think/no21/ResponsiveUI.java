package com.think.no21;

import java.io.IOException;

class UnresponsiveUI{
	private volatile double d =1;
	public UnresponsiveUI() throws Exception {
		while(d>0)
			d=d+(Math.PI+Math.E	)/d;
		System.out.println("a");
		System.in.read();
	}
}

public class ResponsiveUI extends Thread{
	private static volatile double d =1;
	public ResponsiveUI(){
		setDaemon(true);
		start();
	}
	@Override
	public void run() {
		while(true)
			d=d+(Math.PI+Math.E	)/d;
	}
	public static void main(String[] args) throws Exception {
		new UnresponsiveUI();
		new ResponsiveUI();
		System.in.read();
		System.out.println(d);
	}
}
