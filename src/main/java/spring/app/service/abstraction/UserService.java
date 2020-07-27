package spring.app.service.abstraction;

import spring.app.dto.ReviewerDto;
import spring.app.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    void addUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUserById(Long id);

    User getByVkId(Integer vkId);

    boolean isExistByVkId(Integer vkId);

    void deleteUserByVkId(Integer vkId);

    List<User> getUsersByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd);

    List<User> getStudentsByReviewId(Long reviewId);

    List<User> getStudentsByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd);

    List<ReviewerDto> getExaminersInThisTheme (long themeId) ;

    List<ReviewerDto> getExaminersInNotThisTheme (long themeId) ;

    void deleteReviewerByThemeId (long themeId , long examinerId);

    ReviewerDto addNewReviewer (long themeId , ReviewerDto reviewerDto);
}
