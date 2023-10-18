package bank.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.model.Account;
import bank.model.Client;

/**
 * Session Bean implementation class BankService
 */
@Stateless
public class BankService implements BankServiceLocal {
	
	@EJB
	private ClientDao clientDao;
	@EJB
	private AccountDao accountDao;
	

    @Override
	public void createClient(Client client) {
    	clientDao.create(client);
    }
    
    @Override
	public void createAccountForClient(Account account, int clientId) throws BankException {
    	
    	Client client = clientDao.findById(clientId);
    	if(client == null)
    		throw new BankException(String.format("Client with id %d does not exist.", clientId));
    	
    	
    	account.setCreatedate(new Date());
    	client.addAccount(account);
    	accountDao.create(account);
    }
    
    @Override
	public void transfer(int fromAccountId, int toAccountId, double amount) throws BankException {
    	
    }
}
