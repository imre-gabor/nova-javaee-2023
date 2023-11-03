package bank.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import bank.model.Account;
import bank.model.Account_;
import bank.model.Client_;
import bank.model.QAccount;

@Stateless
public class AccountDao extends AbstractDao<Account, Integer> {

	@PersistenceContext
	private EntityManager em;
	private JPAQueryFactory queryFactory;

	public AccountDao() {
		super(Account.class);
	}
	
	@PostConstruct
	public void init() {
		queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);	
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
		if (accountid != null) {
			predicates.add(cb.equal(root.get(Account_.accountid), accountid));
		}

		if (balance != 0.0) {
			predicates.add(cb.between(root.get(Account_.balance), balance * 0.95, balance * 1.05));
		}

		if (createdate != null) {
			Date day = getDayOnly(createdate);
			predicates.add(cb.equal(root.get(Account_.createdate), day));
		}

		if (clientId != null) {
			predicates.add(cb.equal(root.get(Account_.client).get(Client_.clientid), clientId));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

		return em.createQuery(cq).getResultList();
	}

	private Date getDayOnly(Date createdate) {
		LocalDateTime dayOnly = createdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
				.truncatedTo(ChronoUnit.DAYS);

		Date day = Timestamp.valueOf(dayOnly);
		return day;
	}
	
	public List<Account> findByExample2(Account example) {
		Integer accountid = example.getAccountid();
		double balance = example.getBalance();
		Date createdate = example.getCreatedate();
		Integer clientId = example.getClient() == null ? null : example.getClient().getClientid();
		
		QAccount account = QAccount.account;
		
		List<com.querydsl.core.types.Predicate> predicates = new ArrayList<>();
		
		if (accountid != null) {
			predicates.add(account.accountid.eq(example.getAccountid()));
		}

		if (balance != 0.0) {
			predicates.add(account.balance.between(balance*0.95, balance*1.05));
		}

		if (createdate != null) {
			predicates.add(account.createdate.eq(getDayOnly(example.getCreatedate())));
		}

		if (clientId != null) {
			predicates.add(account.client.clientid.eq(example.getClient().getClientid()));
		}
		
		return queryFactory.selectFrom(account)
			.where(ExpressionUtils.allOf(predicates))
//		.where(
//			account.accountid.eq(example.getAccountid())
//			.and(account.balance.between(balance*0.95, balance*1.05))
//			.and(account.createdate.eq(getDayOnly(example.getCreatedate())))
//			.and(account.client.clientid.eq(example.getClient().getClientid()))
//		)
		.fetch();
		
	}
}
