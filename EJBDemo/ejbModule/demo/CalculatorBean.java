package demo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class CalculatorBean
 */
@Stateless
public class CalculatorBean implements CalculatorBeanRemote {

	/**
	 * Default constructor.
	 */
	public CalculatorBean() {
		System.out.println("Calc constructor called");
	}

	@PostConstruct
	public void init() {
		System.out.println("Init called");
	}

	@PreDestroy
	public void destroy() {
		System.out.println("Destroy called");
	}

	@Override
	public int add(int a, int b) {
		return a + b;
	}
}
