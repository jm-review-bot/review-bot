package spring.app.dao.abstraction;

import spring.app.model.User;

import javax.persistence.NoResultException;

public interface UserDao extends GenericDao<Long, User> {
    User getByVkId(Integer vkId) throws NoResultException;

    void deleteUserByVkId(Integer vkId) throws NoResultException ;
}
