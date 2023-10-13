package demo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;

/**
 * Session Bean implementation class CartBean
 */
@Stateful
public class CartBean implements CartBeanRemote {
	
	private List<Integer> productIds = new ArrayList<>();

    /**
     * Default constructor. 
     */
    public CartBean() {
    	System.out.println("CartBean constructor called");
    }

    @PostConstruct
	public void init() {
		System.out.println("CartBean Init called");
	}

	@PreDestroy
	public void destroy() {
		System.out.println("CartBean Destroy called");
	}
	
	@Override
	public void addProduct(Integer productId) {
		productIds.add(productId);
	}
	
	
	@Override
	public List<Integer> getProductIds() {
		//lokál interfészes hívásnál hasznos lehet
		//return Collections.unmodifiableList(productIds);
		return productIds;
	}
}
