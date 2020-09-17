package spring.app.dao.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class AbstractDao<PK extends Serializable, T> {

    @PersistenceContext
    EntityManager entityManager;

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    AbstractDao(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
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
        String genericClassName = persistentClass.toGenericString();
        genericClassName = genericClassName.substring(genericClassName.lastIndexOf('.') + 1);
        List<T> entitys = new ArrayList<>();
        final String finalGenericClassName = genericClassName;
        ids.forEach(id -> {
            Query query = entityManager.createQuery("SELECT e FROM " + finalGenericClassName + " e WHERE e.id = :id");
            query.setParameter("id", id);
            entitys.add((T) query.getResultList());
        });
        return entitys;
    }
}