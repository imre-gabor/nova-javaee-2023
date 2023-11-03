package hu.bme.aait;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Message-Driven Bean implementation class for: DeleteFlightsMDB
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "java:global/jms/ViccAirDFlights"), @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Topic")
		}, 
		mappedName = "java:global/jms/ViccAirDFlights")
public class DeleteFlightsMDB implements MessageListener {

	@EJB
	private OrderHandlerBean orderHandlerBean;
	
    public void onMessage(Message message) {
        try {
			String flightId = ((TextMessage)message).getText();
			orderHandlerBean.cancelFlight(flightId);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
        
    }

}
