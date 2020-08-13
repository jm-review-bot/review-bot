package spring.app.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.userdetails.UserDetails;
import spring.app.core.StepSelector;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
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

    @Column(name = "is_deleted")
    private boolean isDeleted;

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

    @Override
    public Collection<Role> getAuthorities() {
        return Arrays.asList(role);
    }

    // Метод нужен для реализации UserDetails.В рамках проекта username - это VkId пользователя
    @Override
    public String getUsername() {
        return vkId.toString();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
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
