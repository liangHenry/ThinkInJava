package com.think.no21.no3;
/**
 *   SerialNumberGenerator与你想象的一样简单，如果你有C++或其他低层语音的背景，那么可能会期望
 * 递增是原子性操作，因为C++递增通常可以作为一条微处理器指令来实现（尽管不是以任何可靠的、跨平台的形式实现）。
 * 然而正如前面注意到的，Java递增操作不是原子性的，并且涉及一个读操作和一个写操作，所以即使是在这么简单的操作中，
 * 也为产生线程问题留下了空间。正如你所看到的，易变性在这里实际上不是什么问题，真正的问题在于nextSerialNumber()
 * 在没有同步的情况下对共享可变值进行了访问。
 *   基本上，如果一个域可能会被多个任务同时访问，或者这些任务中至少有一个是写入任务，那么你就应该将这个域设置为
 * volatile的。如果你将一个域定义为volatile，那么它就会告诉编译器不要执行任何移除读取和写入操作的优化，
 * 这些操作的目的是用线程中的局部变量维护对这个域的精确同步。实际上，读取和写入都是直接针对内存，而却没有缓存。
 * 但是，volatile并不能对递增不是原子性操这一事实产生影响。
 * 
 * @create @author Henry @date 2016-11-29
 *
 */
public class SerialNumberGenerator {
	private static volatile int serialNumber=0;
	public static int nextSerialNumber(){
		return serialNumber++;
	}
}
