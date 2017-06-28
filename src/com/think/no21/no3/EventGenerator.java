package com.think.no21.no3;
/**
 * һ�������п�������һ������ִ�е�һ����currentEvenValue�ĵ�������֮��
 * ����û��ִ�еڶ�������֮ǰ������next()���������������б�ע��Ϊ
 * "Danger point here!"�ĵط������⽫ʹ���ֵ����"��ǡ��"��״̬��Ϊ��֤�����ǿ��ܷ����ģ�
 * EvenChecker.test()������һ��EvenChecker�����������ض�ȡ�����ͬһ��
 * EvenGenerator,�����Լ��ÿ����ֵ�Ƿ���ż����������ǣ��ͻᱨ����󣬶�����Ҳ���رա�
 * 
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
public class EventGenerator extends IntGenerator{
	private int currentEvenValue=0;
	/**
	 * ���������дһ�������������ܽ�����������һ���̶߳�ȡ��
	 * �������ڶ�ȡһ���ϴ��Ѿ�����һ���߳�д���ı�������ô�����ʹ��ͬ�������ң�
	 * ��ȡ�̶߳�����ʹ����ͬ�ļ�������ͬ����
	 * 
	 * @create @author Henry @date 2016-11-24
	 */
	@Override
	public int next() {//synchronized
		++currentEvenValue;//Danger point here!
		//Thread.yield();
		++currentEvenValue;
		return currentEvenValue;
	}
	/**
	 * ����������ս�ʧ�ܣ���Ϊ����EvenChecker������EvenGenerator����"��ǡ����"״̬ʱ��
	 * ���ܹ��������е���Ϣ�����ǣ�������ʹ�õ��ض�����ϵͳ������ʵ��ϸ�ڣ�ֱ��
	 * EvenCenerator��ɶ��ѭ��֮ǰ��������ⶼ���ᱻ̽�⵽�������ϣ������ط���
	 * ʧ�ܣ����Գ����Ž���yield()�ĵ��÷����ڵ�һ���͵ڶ�����������֮�䡣��ֻ�ǲ�������Ĳ�������
	 * ���ʧ�ܵĸ��ʷǳ��ͣ���ô��ʹ����ȱ�ݣ�����Ҳ���ܿ���������ȷ�� ��
	 * 
	 * ��һ�����Ҫ���Ǿ���Ҫע�⵽������������Ҳ��Ҫ������裬�����ڵ�������������
	 * ���ܻᱻ�̻߳��ƹ���---Ҳ����˵����Java�У���������ԭ���ԵĲ�������Ϊ�����
	 * ���������񣬼�ʹ��һ�ĵ���Ҳ�ǲ���ȫ�ġ�
	 * 
	 * @create @author Henry @date 2016-11-24
	 * @param args
	 */
	public static void main(String[] args) {
		EvenChecker.test(new EventGenerator());
	}
}
