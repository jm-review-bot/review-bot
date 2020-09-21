package spring.app.dao.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractDao<PK extends Serializable, T> {

    @PersistenceContext
    EntityManager entityManager;

    private final Class<T> persistentClass;
    private String genericClassName;


    @SuppressWarnings("unchecked")
    AbstractDao(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
        this.genericClassName = persistentClass.toGenericString();
        genericClassName = genericClassName.substring(genericClassName.lastIndexOf('.') + 1);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void save(T entity) {
        entityManager.persist(entity);
    }

    public T getById(PK id) {
        return entityManager.find(persistentClass, id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteById(PK id) {
        T entity = entityManager.find(persistentClass, id);
        entityManager.remove(entity);
    }

    public List<T> getAll() {
        String genericClassName = persistentClass.toGenericString();
        genericClassName = genericClassName.substring(genericClassName.lastIndexOf('.') + 1);
        String hql = "FROM " + genericClassName;
        TypedQuery<T> query = entityManager.createQuery(hql, persistentClass);
        return query.getResultList();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void removeAll(List<T> entities) {
        for (T entity : entities) {
            entityManager.remove(entity);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> getAllByIds(List<Long> ids) {
        return entityManager.createQuery("FROM " + genericClassName + " e WHERE e.id IN :ids")
                .setParameter("ids", ids)
                .getResultList();
    }

}