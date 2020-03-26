package spring.app.service.abstraction;

import spring.app.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    void addUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUserById(Long id);

    List<User> getUsersByReviewPeriod(LocalDateTime periodStart, LocalDateTime periodEnd);
}
