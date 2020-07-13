package spring.app.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import spring.app.listener.StudentReviewListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(StudentReviewListener.class)
@Table(name = "student_review")
@ApiModel(value = "Ревью студента")
public class StudentReview {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @ApiModelProperty(notes = "Генерируемый базой данных идентификатор для ревью")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @ApiModelProperty(notes = "Id студента")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @ApiModelProperty(notes = "Id ревью")
    private Review review;

    private Boolean isPassed;

    public StudentReview() {
    }

    public StudentReview(User user, Review review, Boolean isPassed) {
        this.user = user;
        this.review = review;
        this.isPassed = isPassed;
    }

    public StudentReview(User user, Review review) {
        this.user = user;
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Boolean getPassed() {
        return isPassed;
    }

    public void setPassed(Boolean passed) {
        isPassed = passed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentReview that = (StudentReview) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
