package bank.service;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import bank.model.Account;
import bank.model.Client;

@Local
public interface BankServiceLocal {

	void transfer(int fromAccountId, int toAccountId, double amount) throws BankException;

	void createAccountForClient(Account account, int clientId) throws BankException;

	void createClient(Client client);

	int logTransfer(int fromAccountId, int toAccountId, double amount);

	void logTransferResult(int historyId, boolean success);

	void scheduleTransfer(int fromAccountId, int toAccountId, double amount, long delayInSec);

}
