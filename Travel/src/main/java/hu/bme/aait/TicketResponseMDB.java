package hu.bme.aait;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import hu.bme.aait.Orders.Status;
import hu.bme.aait.vicc_air.TicketOrderBean;

/**
 * Message-Driven Bean implementation class for: TicketResponseMDB
 */
@MessageDriven(
		activationConfig = { 
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:global/jms/ViccAirResponse"), 
			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		}
//		, 
//		mappedName = "java:global/jms/ViccAirResponse"
		)
public class TicketResponseMDB implements MessageListener {

	@EJB
	OrderHandlerBean orderHandlerBean;
	
    public void onMessage(Message message) {
        try {
			TicketOrderBean ticketOrderBean = ((ObjectMessage)message).getBody(TicketOrderBean.class);
			orderHandlerBean.updateOrderStatus(ticketOrderBean.getOrderId(), mapStatus(ticketOrderBean.getStatus()));
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
        
    }

	private Status mapStatus(hu.bme.aait.vicc_air.TicketOrderBean.Status status) {
		return Enum.valueOf(Status.class, status.name());
	}

}
