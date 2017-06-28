package com.think.no21.no5;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 这是一个简单的例子，两个任务使用一个管道进行通信：
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
 * Sender和Receiver代表了需要互相通信两个任务。Sender创建了一个PipedWriter，它是一个单独的对象；但是对于Received，
 * PipedReader的建立必须在构造器中与一个PipedWriter相关联。Sender把数据放进Writer,然后休眠一段时间（随机数）。然而，
 * Receiver没有sleep()和wait()。相当它调用read()时，如果没有更多的数据，管道将自动阻塞。
 * 
 * 注意sender和receiver是在main()中启动的，即对象构造彻底完毕以后。如果你启动了一个没有构造完毕的对象，在不同的平台上
 * 管道可能会产生不一致的行为（注意,BlockingQueue使用起来更加健壮而容易）。
 * 
 * 在shutdownNow()被调用时，可以看到PipedReader与普通I/O之间最重要的差异--PipedReader是可中断的。如果你将in.read()
 * 调用修改为System.in.read()，那么interrupt()将不能打断read()调用。
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
