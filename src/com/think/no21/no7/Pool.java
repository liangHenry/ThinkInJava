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
 * ��������������cuncurrent.locks���ڽ���synchronized��)���κ�ʱ��
 * ��ֻ����һ���������һ����Դ���������ź�������n������ͬʱ���������Դ��
 * �㻹���Խ��ź���������������ַ�ʹ����Դ�ġ����֤��������ʵ����û��ʹ���κ����֤����
 * 
 * ��Ϊһ��ʾ�����뿼�Ƕ���صĸ�����������������޵Ķ��󣬵�Ҫʹ�ö���ʱ����ǩ�����ǣ�
 * �����û�ʹ�����ʱ�����Խ�����ǩ�ء����ֹ��ܿ��Ա���װ��һ���������С�
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
		//������򻯵���ʽ�У�������ʹ��newInstance()���Ѷ�����ص����С�
		//load pool with objects that can be checked out;
		for(int i=0;i<size;i++)
			try{
				//Assumes a default constructor;
				items.add(classObject.newInstance());
			}catch(Exception e){
				throw new RuntimeException(e);
			}
	}
	//�������Ҫһ���¶�����ô���Ե���checkOut()��������ʹ����֮�󣬽���ݽ���checkIn()��
	public T checkOut() throws InterruptedException{
		available.acquire();
		return getItem();
	}
	
	public void checkIn(T t){
		if(releaseItem(t))
			available.release();
	}
	//boolean���͵�����checkedOut���Ը��ٱ�����Ķ��󣬲��ҿ���ͨ��getItem()��releaseItem����������
	//����Щ������Semaphore���͵�available������ȷ������ˣ���checkOut()�У����û���κ��ź������֤����(����ζ���ڳ���û�и���Ķ�����)��
	//available���������ù��̡���checkIn()�У������ǩ��Ķ�����Ч��������ź�������һ�����֤��
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
