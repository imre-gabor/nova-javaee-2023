package demo;

import javax.ejb.Remote;

@Remote
public interface CalculatorBeanRemote {

	int add(int a, int b);

}
