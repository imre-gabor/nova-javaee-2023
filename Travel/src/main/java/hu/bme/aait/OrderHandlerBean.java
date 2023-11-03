package hu.bme.aait;

import java.util.List;

import hu.bme.aait.Orders.Status;
import hu.bme.aait.vicc_air.TicketOrderBean;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class OrderHandlerBean {
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private JMSContext jmsCtx;
	
	@Resource(mappedName = "java:global/jms/ViccAirRequest")
	private Queue requestDest;

	public void createNewOrder(Orders orderData) {

		// persist orderData with PENDING status
		orderData.setStatus(Status.PENDING);
		em.persist(orderData);
		TicketOrderBean ticketOrderBean = new TicketOrderBean(orderData.getOrderId(), orderData.getCustomername(), 
				orderData.getFlightId(), orderData.getDepart(), orderData.getSeats());
		
		// send JMS msg to ViccAir
		jmsCtx.createProducer().send(requestDest, ticketOrderBean);
	}

	public List<Orders> getOrders() {		
		return em.createQuery("SELECT o FROM Orders o", Orders.class).getResultList();
	}

	public void updateOrderStatus(int orderId, Status status) {
		// update status of the specified order
		Orders order = em.find(Orders.class, orderId);
		order.setStatus(status);
	}
	
	public void cancelFlight(String flightId) {
		// update status to cancelled for all affected orders
		em.createQuery("UPDATE Orders o SET o.status=:status WHERE o.flightId=:flightId")
		.setParameter("status", Status.CANCELLED)
		.setParameter("flightId", flightId)
		.executeUpdate();
	}
}
