package com.think.no21.no5;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ����һ���򵥵����ӣ���������ʹ��һ���ܵ�����ͨ�ţ�
 * 
 * @create @author Henry @date 2016-12-23
 * 
 */
class Sender implements Runnable {
	private Random rand = new Random(47);
	private PipedWriter out = new PipedWriter();
	
	public PipedWriter getPipedWriter(){
		return out;
	}

	@Override
	public void run() {
		try{
			while(true)
				for(char c='A';c<='z';c++){
					out.write(c);
					TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
				}
		}catch(IOException e){
			System.err.println(e+" Sender write exception");
		} catch (InterruptedException e) {
			System.err.println(e+" Sender sleep interrupted");
		}
	}
}

class Receiver implements Runnable{
	private PipedReader in;
	public Receiver(Sender sender) throws IOException{
		in=new PipedReader(sender.getPipedWriter());
	}
	@Override
	public void run() {
		try{
			while(true){
				//Blocks until characters are there
				System.out.println("Read: "+(char)in.read()+". ");
			}
		}catch(IOException e){
			System.err.println(e+" Receiver read exception");
		}
	}
	
}
/**
 * Sender��Receiver��������Ҫ����ͨ����������Sender������һ��PipedWriter������һ�������Ķ��󣻵��Ƕ���Received��
 * PipedReader�Ľ��������ڹ���������һ��PipedWriter�������Sender�����ݷŽ�Writer,Ȼ������һ��ʱ�䣨���������Ȼ����
 * Receiverû��sleep()��wait()���൱������read()ʱ�����û�и�������ݣ��ܵ����Զ�������
 * 
 * ע��sender��receiver����main()�������ģ��������쳹������Ժ������������һ��û�й�����ϵĶ����ڲ�ͬ��ƽ̨��
 * �ܵ����ܻ������һ�µ���Ϊ��ע��,BlockingQueueʹ���������ӽ�׳�����ף���
 * 
 * ��shutdownNow()������ʱ�����Կ���PipedReader����ͨI/O֮������Ҫ�Ĳ���--PipedReader�ǿ��жϵġ�����㽫in.read()
 * �����޸�ΪSystem.in.read()����ôinterrupt()�����ܴ��read()���á�
 * 
 * @create @author Henry @date 2016-12-23
 * 
 */
public class PipedIO {
	public static void main(String[] args) throws Exception {
		Sender sender =new Sender();
		Receiver receiver=new Receiver(sender);
		ExecutorService exec=Executors.newCachedThreadPool();
		exec.execute(sender);
		exec.execute(receiver);
		TimeUnit.SECONDS.sleep(4);
		exec.shutdownNow();
	}
}
