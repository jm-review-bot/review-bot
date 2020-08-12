package spring.app.model;


import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student_feedback")
@Getter
@Setter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_review_id")
    private StudentReview studentReview;

    @Column(name = "rating_reviewer")
    private Integer ratingReviewer;

    @Column(name = "rating_review")
    private Integer ratingReview;

    @Column(name = "student_comment")
    private String comment;

    public Feedback() {
    }
}
