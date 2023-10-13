package demo;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface CartBeanRemote {

	List<Integer> getProductIds();

	void addProduct(Integer productId);

}
