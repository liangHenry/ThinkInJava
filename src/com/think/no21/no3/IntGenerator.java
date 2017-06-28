package com.think.no21.no3;

/**
 * ˵��: ����һ���������ż��������������������Щ���֡�
 * ��������������Ψһ�������Ǽ��ż������Ч�ԡ�
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
/**
 * ���ȣ����Ƕ���EvenChecker����������������Ϊ������������е�
 * ʾ���б����á�Ϊ�˽�EvenChecker������Ҫ����ĸ������͵�������
 * ������ǽ�����һ����ΪIntGenerator�ĳ����࣬������EvenChecker
 * �����˽�ıز����ٵķ�������һ��Next()��������һ������ִ�г����ķ�����
 * �����û��ʵ��Generator�ӿڣ���Ϊ���������һ��int,�����Ͳ�֧�ֻ������͵Ĳ���
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
public abstract class IntGenerator {
	private volatile boolean canceled = false;

	public abstract int next();
	
	public void cancel() {
		canceled = true;
	}

	public boolean isCanceled() {
		return canceled;
	}
}
