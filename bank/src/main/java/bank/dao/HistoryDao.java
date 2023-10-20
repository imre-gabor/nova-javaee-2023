package bank.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import bank.model.History;

@Stateless
public class HistoryDao extends AbstractDao<History, Integer>{

	@PersistenceContext
	private EntityManager em;
	
	public HistoryDao() {
		super(History.class);
	}

	@Override
	public EntityManager em() {
		return em;
	}
}
