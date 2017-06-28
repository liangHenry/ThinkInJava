package com.think.no21.no7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
/**
 * Semaphore 
 * 
 * @author zhaoliang
 * @date 2017-05-23
 * 
 * 正常的锁（来自cuncurrent.locks或内建的synchronized锁)在任何时刻
 * 都只允许一个任务访问一项资源，而计数信号量允许n个任务同时访问这个资源。
 * 你还可以将信号量看做是在向外分发使用资源的“许可证”，尽管实际上没有使用任何许可证对象。
 * 
 * 作为一个示例，请考虑对象池的概念，它管理着数量有限的对象，当要使用对象时可以签出他们，
 * 而在用户使用完毕时，可以将它们签回。这种功能可以被封装到一个泛型类中。
 *
 * @param <T>
 */
public class Pool<T> {
	private int size;
	private List<T> items=new ArrayList<T>();
	private volatile boolean[] checkedOut;
	private Semaphore available;
	public Pool(Class<T> classObject,int size){
		this.size=size;
		checkedOut=new boolean[size];
		available =new Semaphore(size, true);
		//在这个简化的形式中，构造器使用newInstance()来把对象加载到池中。
		//load pool with objects that can be checked out;
		for(int i=0;i<size;i++)
			try{
				//Assumes a default constructor;
				items.add(classObject.newInstance());
			}catch(Exception e){
				throw new RuntimeException(e);
			}
	}
	//如果你需要一个新对象，那么可以调用checkOut()，并且在使用完之后，将其递交给checkIn()。
	public T checkOut() throws InterruptedException{
		available.acquire();
		return getItem();
	}
	
	public void checkIn(T t){
		if(releaseItem(t))
			available.release();
	}
	//boolean类型的数组checkedOut可以跟踪被检出的对象，并且可以通过getItem()和releaseItem方法来管理。
	//而这些都将由Semaphore类型的available来加以确保，因此，在checkOut()中，如果没有任何信号量许可证可用(这意味着在池中没有更多的对象了)。
	//available将阻塞调用过程。在checkIn()中，如果被签入的对象有效，则会向信号量返回一个许可证。
	private synchronized T getItem(){
		for(int i=0;i<size;++i)
			if(!checkedOut[i]){
				checkedOut[i]=true;
				return items.get(i);
			}
		return null;
	}
	private synchronized boolean releaseItem(T item){
		int index=items.indexOf(item);
		if(index==-1) return false;
		if(checkedOut[index]){
			checkedOut[index]=false;
			return true;
		}
		return false;//Wasn't checked out 
	}
}
