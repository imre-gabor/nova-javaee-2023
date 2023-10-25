package bank.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import bank.dao.HistoryDao;
import bank.model.History;

@Stateless
public class HistoryService {

	@EJB
	private HistoryDao historyDao;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void logTransfer(int fromAccountId, int toAccountId, double amount) {
		historyDao
				.create(new History(String.format("Transfering %f from %d to %d", amount, fromAccountId, toAccountId)));
	}
		
}
