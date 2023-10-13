package clientapp;

import javax.ejb.EJB;

import demo.CalculatorBeanRemote;
import demo.CartBeanRemote;

public class Main {
	
	@EJB
	static CalculatorBeanRemote calc;
	
	@EJB
	static CartBeanRemote cart1;
	
	@EJB
	static CartBeanRemote cart2;
	
	
	public static void main(String[] args) {
		System.out.println("7+8=" + calc.add(7, 8));
		cart1.addProduct(1);
		cart1.addProduct(3);
		cart1.addProduct(5);
		cart2.addProduct(2);
		cart2.addProduct(4);
		cart2.addProduct(6);
		
		System.out.println("Cart 1:");
		printCart(cart1);
		System.out.println("Cart 2:");
		printCart(cart2);
	}


	private static void printCart(CartBeanRemote cart) {
		cart.getProductIds().forEach(System.out::println);
	}


	/* (non-Java-doc)
	 * @see java.lang.Object#Object()
	 */
	public Main() {
		super();
	}

}