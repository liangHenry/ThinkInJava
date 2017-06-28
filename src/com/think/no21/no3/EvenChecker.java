package com.think.no21.no3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * �ڱ����п��Ա��������಻��Runnable��������������IntGenerator�����EvenChecker����
 * �����������Բ鿴���Ƿ��Ѿ�����������������run()��������ͨ�����ַ�ʽ����������Դ��IntGenerator��
 * ��������Թ۲����Դ����ֹ�źš������������ν������������������������������Ӧĳ��������
 * ��˲�����ͻ��һ�½���������
 * �������ϸ���ǲ���������ϵͳʧ�ܵ����п���;����
 * ���磬һ����������������һ��������Ϊ����رյ�˳���޷��õ���֤��
 * ����ͨ��ʹ���������ڷ�����������ǿ�������Ǳ�ڵľ���������
 * 
 * @create @author Henry @date 2016-11-24
 *
 */
public class EvenChecker implements Runnable{
	private IntGenerator generator;
	private final int id;
	
	public EvenChecker(IntGenerator generator, int id) {
		this.generator = generator;
		this.id = id;
	}
	/**
	 * EvenChecker�������Ƕ�ȡ�Ͳ��Դ�������ص�IntGenerator���ص�ֵ��ע�⣬
	 * ���generator.isCanceled()Ϊtrue����run()�����أ��⽫��֪EvenChecker.test()
	 * �е�Executor����������ˡ��κ�EvenChecker���񶼿����������������IntGenerator
	 * �ϵ���cancel()���⽫������������ʹ�ø�IntGenerator��EvenChecker����عرա�
	 * �ں�������У��㽫����Java�����������߳���ֹ�ĸ��ָ�ͨ�õĻ��ơ�
	 * 
	 * @create @author Henry @date 2016-11-24
	 */
	@Override
	public void run() {
		while(!generator.isCanceled()){
			int val=generator.next();
			if(val%2!=0){
				System.out.println(val+" not even!");
				generator.cancel();//Cancels all EvenCheckers
			}
		}
	}
	/**
	 * test() ����ͨ����������ʹ����ͬ��IntGenerator��EvenChecker,���ò�ִ�ж��κ����͵�
	 * IntGenerator�Ĳ��ԡ����IntGenerator����ʧ�ܣ���ôtest()�������������أ�
	 * ����,����밴��Control-C����ֹ����
	 * 
	 * @create @author Henry @date 2016-11-24
	 * @param gp
	 * @param count
	 */
	public static void test(IntGenerator gp,int count){
		System.out.println("Press Control-C to exit");
		ExecutorService exec=Executors.newCachedThreadPool();
		for (int i = 0; i < count; i++) 
			exec.execute(new EvenChecker(gp, i));
		exec.shutdown();
	}
	public static void test(IntGenerator gp){
		test(gp, 10);
	}
}
