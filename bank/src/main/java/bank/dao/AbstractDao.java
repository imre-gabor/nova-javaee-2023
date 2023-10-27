package bank.dao;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

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
	
	//7. megoldás LazyInit ellen
	public List<T> findAllWithFetch(PluralAttribute<T, ?, ?> relationship) {
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		Root<T> root = cq.from(entityClass);
		root.fetch(relationship);
		cq.distinct(true);
		return em().createQuery(cq).getResultList();
	}
	
	//8. megoldás LazyInit ellen
	public List<T> findAllWithEntityGraph(String egName) {
		CriteriaBuilder cb = em().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		cq.from(entityClass);
		
		EntityGraph<?> entityGraph = em().getEntityGraph(egName);
		
		return em().createQuery(cq)
				.setHint("javax.persistence.loadgraph", entityGraph)
				.getResultList();
	}
}
