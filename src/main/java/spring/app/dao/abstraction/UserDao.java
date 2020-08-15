package spring.app.dao.abstraction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dto.ReviewerDto;
import spring.app.dto.UserDto;
import spring.app.model.User;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

public interface UserDao extends GenericDao<Long, User> {

    User getByVkId(Integer vkId) throws NoResultException;

    boolean isExistByVkId(Integer vkId);

    @Transactional(propagation = Propagation.MANDATORY)
    void deleteUserByVkId(Integer vkId) throws NoResultException;

    List<User> getUsersByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd);

    List<User> getStudentsByReviewId(Long reviewId);

    List<User> getStudentsByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd);

    boolean isUserExaminer(Long userId);

    List<ReviewerDto> getExaminersInThisTheme (long themeId) ;

    List<ReviewerDto> getExaminersInNotThisTheme (long themeId) ;

    @Transactional(propagation = Propagation.MANDATORY)
    User addNewReviewer (User user);

    @Transactional(propagation = Propagation.MANDATORY)
    void deleteReviewerFromTheme (long themeId , long reviewerId);

    List<User> getExaminersByFreeThemeId(Long freeThemeId) ;

    List<UserDto> usersSearch(String searchString);
}
