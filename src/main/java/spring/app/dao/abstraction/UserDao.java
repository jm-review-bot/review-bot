package spring.app.dao.abstraction;

import spring.app.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserDao extends GenericDao<Long, User> {
    List<User> getUsersByReviewDate(LocalDateTime now);
}
