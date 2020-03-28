package spring.app.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "vk_id")
    private Integer vkId;

    /**
     * Текущий(последний) шаг пользователя в чатботе
     */
    @Column(name = "chat_step")
    private String chatStep;

    /**
     * Факт просмотра пользователем стартового сообщения шага.
     * false - показываем ему сообщение
     * true - ждем от него сообщения, обрабатываем его input
     */
    @Column(name = "is_viewed")
    private boolean isViewed;

    @Column(name = "review_point")
    private Integer reviewPoint;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User() {
    }

    public User(String firstName, String lastName, Integer vkId, String chatStep, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.vkId = vkId;
        this.chatStep = chatStep;
        this.role = role;
        this.isViewed = false;
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

    public String getChatStep() {
        return chatStep;
    }

    public void setChatStep(String chatStep) {
        this.chatStep = chatStep;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
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
