package com.think.no21.no4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 幸运的是，在第18章中介绍的各种nio类提供了更人性化的I/O中断。被阻塞的nio通道会自动响应中断。
 * 
 * @create @author Henry @date 2016-12-14
 * 
 */
class NIOBlocked implements Runnable {
	private final SocketChannel sc;

	public NIOBlocked(SocketChannel sc) {
		this.sc = sc;
	}

	@Override
	public void run() {
		try {
			System.out.println("Waiting for read() in " + this);
			sc.read(ByteBuffer.allocate(1));
		} catch (ClosedByInterruptException e) {
			System.out.println("ClosedByInterruptException");
		} catch (AsynchronousCloseException e) {
			System.out.println("AsynchronousCloseException");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Exiting NIOBlocked.run() " + this);
	}
}
/**
 * 如你所见，你还可以关闭底层资源以释放锁，尽管这种做法一般不是必需的。注意，使用execute()来启动两个任务，
 * 并调用e.shutdownNow()将可以很容易地终止所有事物，而对于捕获下面示例中的Future,只有在将终端发送给一个
 * 线程，同时不发送给另一个线程时才是必需的。
 * 
 * 运行结果如下：
 * Waiting for read() in com.think.no21.no4.NIOBlocked@5740bb
 * Waiting for read() in com.think.no21.no4.NIOBlocked@5ac072
 * ClosedByInterruptException
 * Exiting NIOBlocked.run() com.think.no21.no4.NIOBlocked@5740bb
 * AsynchronousCloseException
 * Exiting NIOBlocked.run() com.think.no21.no4.NIOBlocked@5ac072
 * 
 * @create @author Henry @date 2016-12-14
 *
 */
public class NIOInterruption {
	public static void main(String[] args) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(8088);
		InetSocketAddress isa = new InetSocketAddress("localhost", 8088);
		SocketChannel sc1 = SocketChannel.open(isa);
		SocketChannel sc2 = SocketChannel.open(isa);
		Future<?> f = exec.submit(new NIOBlocked(sc1));
		exec.execute(new NIOBlocked(sc2));
		exec.shutdown();
		TimeUnit.SECONDS.sleep(1);
		// Produce and interrupt via cancel;
		f.cancel(true);
		TimeUnit.SECONDS.sleep(1);
		sc2.close();
	}
}
