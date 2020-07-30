package spring.app.model;

import org.springframework.security.core.userdetails.UserDetails;
import spring.app.core.StepSelector;
import spring.app.listener.UserListener;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
@EntityListeners(UserListener.class)
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "vk_id", unique = true)
    private Integer vkId;

    /**
     * Текущий(последний) шаг пользователя в чатботе
     */
    @Column(name = "chat_step")
    @Enumerated(EnumType.ORDINAL)
    private StepSelector chatStep;

    /**
     * Факт просмотра пользователем стартового сообщения шага.
     * false - показываем ему сообщение
     * true - ждем от него сообщения, обрабатываем его input
     */
    @Column(name = "is_viewed")
    private boolean isViewed;

    @Column(name = "review_point")
    private Integer reviewPoint = 0;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "password")
    private String password;

    @Column(name = "is_account_non_expired")
    private boolean isAccountNonExpired;

    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked;

    @Column(name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Column(name = "is_enabled")
    private boolean isEnabled;



    public User() {
    }

    public User(String firstName, String lastName, Integer vkId, StepSelector chatStep, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.vkId = vkId;
        this.chatStep = chatStep;
        this.role = role;
        this.isViewed = false;
    }

    public User(String firstName, String lastName, Integer vkId, StepSelector chatStep) {
        this.vkId = vkId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.chatStep = chatStep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getVkId() {
        return vkId;
    }

    public void setVkId(Integer vkId) {
        this.vkId = vkId;
    }

    public Integer getReviewPoint() {
        return reviewPoint;
    }

    public void setReviewPoint(Integer reviewPoint) {
        this.reviewPoint = reviewPoint;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return Arrays.asList(role);
    }

    public StepSelector getChatStep() {
        return chatStep;
    }

    public void setChatStep(StepSelector chatStep) {
        this.chatStep = chatStep;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }

    // Метод нужен для реализации UserDetails.В рамках проекта username - это VkId пользователя
    @Override
    public String getUsername() {
        return vkId.toString();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;

        return id.equals(user.id) && vkId.equals(user.vkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vkId);
    }
}
