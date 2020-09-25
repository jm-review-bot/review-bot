package spring.app.service.abstraction;

import org.springframework.security.core.userdetails.UserDetailsService;
import spring.app.dto.ReviewerDto;
import spring.app.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    void addUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUserById(Long id);

    Optional<User> getByVkId(Integer vkId);

    boolean isExistByVkId(Integer vkId);

    void deleteUserByVkId(Integer vkId);

    List<User> getUsersByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd);

    List<User> getStudentsByReviewId(Long reviewId);

    List<User> getStudentsByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd);

    boolean isUserExaminer(Long userId);

    List<ReviewerDto> getExaminersInThisTheme (long themeId) ;

    List<ReviewerDto> getExaminersInNotThisTheme (long themeId) ;

    User addNewReviewer (long themeId , long userId);

    void deleteReviewerFromTheme (long themeId , long reviewerId);
}
