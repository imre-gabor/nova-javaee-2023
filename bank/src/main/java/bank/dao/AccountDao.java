package bank.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import bank.model.Account;
import bank.model.Account_;
import bank.model.Client_;

@Stateless
public class AccountDao extends AbstractDao<Account, Integer>{

	@PersistenceContext
	private EntityManager em;
	
	public AccountDao() {
		super(Account.class);
	}

	@Override
	public EntityManager em() {
		return em;
	}
	
	 public List<Account> findByExample(Account example) {
	    	Integer accountid = example.getAccountid();
	    	double balance = example.getBalance();
	    	Date createdate = example.getCreatedate();
	    	Integer clientId = example.getClient() == null ? null : example.getClient().getClientid();
	    	
	    	CriteriaBuilder cb = em.getCriteriaBuilder();
	    	CriteriaQuery<Account> cq = cb.createQuery(Account.class);
	    	Root<Account> root = cq.from(Account.class);
	    	cq.select(root);
	    	
	    	List<Predicate> predicates = new ArrayList<Predicate>();
	    	if(accountid != null) {
	    		predicates.add(cb.equal(root.get(Account_.accountid), accountid));
	    	}
	    	
	    	if(balance != 0.0) {
				predicates.add(cb.between(root.get(Account_.balance), balance * 0.95, balance * 1.05));
	    	}
	    	
	    	if(createdate != null) {
	    		LocalDateTime dayOnly = createdate.toInstant()
	    	      .atZone(ZoneId.systemDefault())
	    	      .toLocalDateTime().truncatedTo(ChronoUnit.DAYS);
	    		
	    		Date day = Timestamp.valueOf(dayOnly);
	    		predicates.add(cb.equal(root.get(Account_.createdate), day));
	    	}
	    	
	    	if(clientId != null) {
	    		predicates.add(cb.equal(root.get(Account_.client).get(Client_.clientid), clientId));
	    	}
	    	
	    	cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
	    	
	    	return em.createQuery(cq).getResultList();
	    }
}
