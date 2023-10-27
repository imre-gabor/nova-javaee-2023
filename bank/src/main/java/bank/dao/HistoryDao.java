package bank.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import bank.model.Client;
import bank.model.History;
import bank.model.History_;

@Stateless
public class HistoryDao extends AbstractDao<History, Integer> {

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
