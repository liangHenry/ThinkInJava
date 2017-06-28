package com.think.no21.no7;

public class StaticInner {
	public static class Inner {
		public static int i;
		{
			System.out.println("hello2");
		}
		static {
			System.out.println("world2");
		}

		public Inner() {
			System.out.println("Inner");
		}
	}

	public StaticInner() {
		System.out.println("outer");
	}

	{
		System.out.println("hello");
	}
	static {
		System.out.println("world");
	}

	public static void main(String[] args) {
		StaticInner.Inner.i=1;
		System.out.println(StaticInner.Inner.i);
	}
}
