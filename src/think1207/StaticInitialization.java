package think1207;

import java.sql.Date;

class Bowl {
	Bowl(int marker) {
		System.out.println("Bowl(" + marker + ")");
	}

	void f1(int marker) {
		System.out.println("f1(" + marker + ")");
	}
}

class Table {
	static Bowl bowl1 = new Bowl(1);

	Table() {
		System.out.println("Table()");
		bowl2.f1(1);
	}

	void f2(int marker) {
		System.out.println("f2(" + marker + ")");
	}

	static Bowl bowl2 = new Bowl(2);
}

class Cupboard {
	Bowl bowl3 = new Bowl(3);
	static Bowl bowl4 = new Bowl(4);

	Cupboard() {
		System.out.println("Cupboard()");
		bowl4.f1(2);
	}

	void f3(int marker) {
		System.out.println("f3(" + marker + ")");
	}

	static Bowl bowl5 = new Bowl(5);
}

public class StaticInitialization {
	public static void main(String[] args) {
		/*
		 * System.out.println("Creating new Cupboard() in main");
		 * new Cupboard();
		 * System.out.println("Creating new Cupboard() in main");
		 * new Cupboard();
		 * table.f2(1);
		 * cupboard.f3(1);
		 
		Date jxr = Date.valueOf("2017-12-25");
		Date shr = Date.valueOf("2016-12-26");
		System.out.println(100/Math.pow(1 + 0, 0.9972602739726028));
		System.out.println(new StaticInitialization().fixedRatePrice(100.0, 0.9972602739726028, 7, 100));
	*/
		}

	public double fixedRatePrice(double P, double l2, double c, double M) {
		double Y = 0D;
		double y1 = -2d;
		double y2 = 2d;
		int i = 1;
		while (i <= 20000) {
			Y = (y1 + y2) / 2;
			double test = 0D;
			for (int j = 1; j <= (int) l2; j++) {
				test += c / Math.pow(1 + Y, j);
			}
			test = test + M / Math.pow(1 + Y, l2);//
			if (test <= P)
				y2 = Y;
			else
				y1 = Y;
			if (y2 - y1 < 0.0000000001)
				break;
			i++;
		}
		return Y;
	}
	/*
	 * static Table table = new Table();
	 * static Cupboard cupboard = new Cupboard();
	 */
}
