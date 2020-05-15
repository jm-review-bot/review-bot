package spring.app.dao.abstraction;

import spring.app.model.User;

import javax.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.List;

public interface UserDao extends GenericDao<Long, User> {

    User getByVkId(Integer vkId) throws NoResultException;

    boolean isExistByVkId(Integer vkId);

    void deleteUserByVkId(Integer vkId) throws NoResultException;

    List<User> getUsersByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd);

    List<User> getStudentsByReviewId(Long reviewId);

    Long getMaxId();
}
