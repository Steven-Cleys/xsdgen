package be.devoowi.brol;

public class Testje {

	public static void main(String ... arg) {
		
		int a = 25;
		Integer b = new Integer(25);
		
		System.out.print("a == b: ");
		if (a == b) {
			System.out.println("TRUE");
		} else {
			System.out.println("FALSE");
		}
		
		
		System.out.print("b.equals(a): ");
		if (b.equals(a)) {
			System.out.println("TRUE");
		} else {
			System.out.println("FALSE");
		}
		
		System.out.print("b == a: ");
		if (b == a) {
			System.out.println("TRUE");
		} else {
			System.out.println("FALSE");
		}
		
	}
	
}
