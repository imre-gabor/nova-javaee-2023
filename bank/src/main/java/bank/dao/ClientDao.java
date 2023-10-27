package bank.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import bank.model.Client;
import bank.model.Client_;

@Stateless
public class ClientDao extends AbstractDao<Client, Integer>{

	@PersistenceContext
	private EntityManager em;
	
	public ClientDao() {
		super(Client.class);
	}

	@Override
	public EntityManager em() {
		return em;
	}

	//1. megoldás a LazyInit ellen: N+1 SELECT probléma
//	@Override
//	public List<Client> findAll() {
//		List<Client> allClients = super.findAll();
//		allClients.forEach(c -> c.getAccounts().size());
//		return allClients;
//	}
	
	
//	@Override
//	public List<Client> findAll() {
//		//5. megoldás LazyInit ellen
////		return em.createNamedQuery("Client.findAllWithAccounts", Client.class).getResultList();
//		//6. megoldás LazyInit ellen
//		CriteriaBuilder cb = em().getCriteriaBuilder();
//		CriteriaQuery<Client> cq = cb.createQuery(Client.class);
//		Root<Client> root = cq.from(Client.class);
//		root.fetch(Client_.accounts, JoinType.LEFT);
//		cq.distinct(true);
//		return em().createQuery(cq).getResultList();
//	}
}
