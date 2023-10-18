package bank.service;

import javax.ejb.Stateless;

import bank.model.Account;
import bank.model.Client;

/**
 * Session Bean implementation class BankService
 */
@Stateless
public class BankService implements BankServiceLocal {

    @Override
	public void createClient(Client client) {
    	
    }
    
    @Override
	public void createAccountForClient(Account account, int clientId) throws BankException {
    	
    }
    
    @Override
	public void transfer(int fromAccountId, int toAccountId, double amount) throws BankException {
    	
    }
}
