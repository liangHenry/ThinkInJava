package com.think.no21.no4;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 对于这类问题，有一个略显笨拙但是有时确实行之有效的解决方案，
 * 即关闭任务在其上发生阻塞的底层资源。
 * 在shutdownNow()被调用之后以及在两个输入流上调用close()之前的延迟强调的是
 * 一旦底层资源被关闭，任务将被解除阻塞。请注意，有一点很有趣，interrupt()看
 * 起来发生在关闭Socket而不是关闭System.in的时刻。
 * 在本人用Eclipse执行时发现System.in 是停不掉的。只有将整个项目停了再能将其停掉。
 * 但是在Doc下运行时，是可以直接停掉。
 * 
 * 运行结果如下：
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
