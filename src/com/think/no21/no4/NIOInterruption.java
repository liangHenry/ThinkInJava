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
 * ���˵��ǣ��ڵ�18���н��ܵĸ���nio���ṩ�˸����Ի���I/O�жϡ���������nioͨ�����Զ���Ӧ�жϡ�
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
 * �����������㻹���Թرյײ���Դ���ͷ�����������������һ�㲻�Ǳ���ġ�ע�⣬ʹ��execute()��������������
 * ������e.shutdownNow()�����Ժ����׵���ֹ������������ڲ�������ʾ���е�Future,ֻ���ڽ��ն˷��͸�һ��
 * �̣߳�ͬʱ�����͸���һ���߳�ʱ���Ǳ���ġ�
 * 
 * ���н�����£�
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
