package bank.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

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
	@Resource
	SessionContext ctx;
	

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
    	
    	Account fromAccount = accountDao.findById(fromAccountId);
    	Account toAccount = accountDao.findById(toAccountId);
    	if(fromAccount == null || toAccount == null)
    		throw new BankException("Both account ids should exists");
    	try {
    		toAccount.increase(amount);
    		fromAccount.decrease(amount);
    	} catch (Exception e) {
			ctx.setRollbackOnly();
			throw e;
		}
    	//nem kell dao.update, tranzakció végén magától DB-be íródik a változás
    	
    	//ha a BankException nem RuntimeException gyermek, by default nincs rollback 
    	//--> increase megtörténik akkor is, ha nincs elég egyenleg
    	/*lehetséges megoldások:
    	 * 1. BankException extends RuntimeException
    	 * 2. @ApplicationException(rollback = true) 
    	 * 3. ejbCtxt.setRollbackOnly()
    	 */
    	
    }
}
