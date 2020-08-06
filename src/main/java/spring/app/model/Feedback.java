package spring.app.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "student_feedback")
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
