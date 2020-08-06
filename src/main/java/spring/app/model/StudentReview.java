package spring.app.model;

import lombok.Getter;
import lombok.Setter;
import spring.app.listener.StudentReviewListener;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@EntityListeners(StudentReviewListener.class)
@Table(name = "student_review")
public class StudentReview {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
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
