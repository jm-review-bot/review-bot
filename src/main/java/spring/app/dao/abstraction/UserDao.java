package spring.app.dao.abstraction;

import spring.app.model.User;

import javax.persistence.NoResultException;

public interface UserDao extends GenericDao<Long, User> {
    User getByVkId(Integer vkId) throws NoResultException;

    boolean isExistByVkId(Integer vkId);

    void deleteUserByVkId(Integer vkId) throws NoResultException;
}
