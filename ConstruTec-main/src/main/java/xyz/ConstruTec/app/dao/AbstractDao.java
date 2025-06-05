package xyz.ConstruTec.app.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractDao<T, PK extends Serializable> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

	@SuppressWarnings("unchecked")
	private final Class<T> entityClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	
	@PersistenceContext
	private EntityManager entityManager;

	protected EntityManager getEntityManager() {
		if (entityManager == null) {
			logger.error("EntityManager não foi injetado!");
			throw new IllegalStateException("EntityManager não foi injetado corretamente");
		}
		return entityManager;
	}
	
	@Transactional
	public void save(T entity) { 
		logger.debug("Salvando entidade: {}", entity);
		getEntityManager().persist(entity);
	}
	
	@Transactional
	public void update(T entity) {
		logger.debug("Atualizando entidade: {}", entity);
		getEntityManager().merge(entity);
	}
	
	@Transactional
	public void delete(PK id) {
		logger.debug("Deletando entidade com ID: {}", id);
		getEntityManager().remove(getEntityManager().getReference(entityClass, id));
	}
	
	@Transactional(readOnly = true)
	public T findById(PK id) {
		logger.debug("Buscando entidade por ID: {}", id);
		return getEntityManager().find(entityClass, id);
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll() {
		logger.debug("Buscando todas as entidades de: {}", entityClass.getSimpleName());
		return getEntityManager()
				.createQuery("from " + entityClass.getSimpleName(), entityClass)
				.getResultList();
	}	
	
	@Transactional(readOnly = true)
	protected List<T> createQuery(String jpql, Object... params) {
		logger.debug("Executando query: {} com parâmetros: {}", jpql, params);
		TypedQuery<T> query = getEntityManager().createQuery(jpql, entityClass);
		for (int i = 0; i < params.length; i++) {
		    query.setParameter(i+1, params[i]);
        }
    	return query.getResultList();
	}
}