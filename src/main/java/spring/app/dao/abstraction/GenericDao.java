package spring.app.dao.abstraction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


public interface GenericDao<PK extends Serializable, T> {

    @Transactional(propagation = Propagation.MANDATORY)
    void save(T entity);

	T getById(PK id);

	List<T> getAll();

    @Transactional(propagation = Propagation.MANDATORY)
    void update(T group);

    @Transactional(propagation = Propagation.MANDATORY)
    void deleteById(PK id);

    @Transactional(propagation = Propagation.MANDATORY)
    void removeAll(List<T> entities);

    List <T> getAllByIds(List<Long> ids);

}
