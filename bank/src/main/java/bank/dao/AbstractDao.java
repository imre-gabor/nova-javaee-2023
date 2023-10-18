package bank.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public abstract class AbstractDao<T, I> {

	private Class<T> entityClass;
	
	public AbstractDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public abstract EntityManager em();
	
	public void create(T entity) {
		em().persist(entity);
	}
	
	public T update(T entity) {
		return em().merge(entity);
	}
	
	public T findById(I id) {
		return em().find(entityClass, id);
	}
	
	public void delete(T entity) {
		em().remove(em().merge(entity));
	}
	
	public List<T> findAll() {
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		cq.from(entityClass);
		return em().createQuery(cq).getResultList();
	}
}
