package spring.app.dao.abstraction;

import spring.app.model.User;

public interface UserDao extends GenericDao<Long, User> {
    User getByVkId(Integer vkId);
}
