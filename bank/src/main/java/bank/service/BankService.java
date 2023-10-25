package bank.service;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import bank.dao.AccountDao;
import bank.dao.ClientDao;
import bank.dao.HistoryDao;
import bank.model.Account;
import bank.model.Client;
import bank.model.History;

/**
 * Session Bean implementation class BankService
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
public class BankService implements BankServiceLocal {
	
	@EJB
	private ClientDao clientDao;
	@EJB
	private AccountDao accountDao;
	@EJB
	private HistoryDao historyDao;
	@EJB
	private HistoryService historyService;
	@Resource
	private SessionContext ctx;
	@Resource
	private TimerService timerService;
	

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
    
    private static class TransferRequest implements Serializable {
    	int fromAccountId;
    	int toAccountId;
    	double amount;
    	
    	public TransferRequest() {
		}
    	
		public TransferRequest(int fromAccountId, int toAccountId, double amount) {
			super();
			this.fromAccountId = fromAccountId;
			this.toAccountId = toAccountId;
			this.amount = amount;
		}
    	
    }
    
    @Override
	public void scheduleTransfer(int fromAccountId, int toAccountId, double amount, long delayInSec) {
    	timerService.createSingleActionTimer(delayInSec * 1000, new TimerConfig(new TransferRequest(fromAccountId, toAccountId, amount), false));
    }
    
    @Timeout
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void executeScheduledTransfer(Timer timer) {
    	TransferRequest transferRequest = (TransferRequest) timer.getInfo();
    	try {
    		transfer(transferRequest.fromAccountId, transferRequest.toAccountId, transferRequest.amount);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    @Override
	public void transfer(int fromAccountId, int toAccountId, double amount) throws BankException {

//		historyService.logTransfer(fromAccountId, toAccountId, amount);
		BankServiceLocal self = ctx.getBusinessObject(BankServiceLocal.class);
		int historyId = self.logTransfer(fromAccountId, toAccountId, amount);

		try {
    	
	    	Account fromAccount = accountDao.findById(fromAccountId);
	    	Account toAccount = accountDao.findById(toAccountId);
	    	if(fromAccount == null || toAccount == null)
	    		throw new BankException("Both account ids should exists");
    	
    		toAccount.increase(amount);
    		fromAccount.decrease(amount);
    		self.logTransferResult(historyId, true);
    	} catch (Exception e) {
			self.logTransferResult(historyId, false);
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
    
    /*
     * A requires_new (és semmilyen tranzakciós vagy security vagy egyéb annotáció) nem működik, 
     * ha lokálisan hívott metóduson van. Lehetséges megoldások:
     * 1. Metódus kiszervezése külön EJB-be
     * 2. Hívás ctx.getBusinessObject() metóduson keresztül
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)    
    public int logTransfer(int fromAccountId, int toAccountId, double amount) {
    	History history = new History(String.format("Transfering %f from %d to %d", amount, fromAccountId, toAccountId));
		historyDao.create(history);
		return history.getId();
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void logTransferResult(int historyId, boolean success) {
    	History history = historyDao.findById(historyId);
    	history.setSuccess(success);
    }
    
}
