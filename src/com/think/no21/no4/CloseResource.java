package com.think.no21.no4;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * �����������⣬��һ�����Ա�׾������ʱȷʵ��֮��Ч�Ľ��������
 * ���ر����������Ϸ��������ĵײ���Դ��
 * ��shutdownNow()������֮���Լ��������������ϵ���close()֮ǰ���ӳ�ǿ������
 * һ���ײ���Դ���رգ����񽫱������������ע�⣬��һ�����Ȥ��interrupt()��
 * ���������ڹر�Socket�����ǹر�System.in��ʱ�̡�
 * �ڱ�����Eclipseִ��ʱ����System.in ��ͣ�����ġ�ֻ�н�������Ŀͣ�����ܽ���ͣ����
 * ������Doc������ʱ���ǿ���ֱ��ͣ����
 * 
 * ���н�����£�
 * Waiting for read():
 * Waiting for read():
 * Shutting down all threads
 * Closing java.net.SocketInputStream
 * Interrupted from blocked I/O
 * Exiting IOBlocked.run()
 * Closing java.io.BufferedInputStream
 * Exiting IOBlocked.run()
 * 
 * @create @author Henry @date 2016-12-14
 * 
 */
public class CloseResource {
	public static void main(String[] args) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(8088);
		InputStream socketInput = new Socket("localhost", 8088).getInputStream();
		exec.execute(new IOBlocked(socketInput));
		exec.execute(new IOBlocked(System.in));
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Shutting down all threads");
		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing " + socketInput.getClass().getName());
		socketInput.close();// Releases blocked thread
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing " + System.in.getClass().getName());
		System.in.close();// Releases blocked thread
	}
}
