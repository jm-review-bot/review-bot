package spring.app.model;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

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
    @JoinColumn(name = "review_id")
    private StudentReview studentReview;

    @Column(name = "rating_reviewer")
    private Integer ratingReviewer;

    @Column(name = "rating_review")
    private Integer ratingReview;

    @Column(name = "student_comment")
    private String comment;

    public Feedback() {
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

    public StudentReview getStudentReview() {
        return studentReview;
    }

    public void setStudentReview(StudentReview studentReview) {
        this.studentReview = studentReview;
    }

    public Integer getRatingReviewer() {
        return ratingReviewer;
    }

    public void setRatingReviewer(Integer ratingReviewer) {
        this.ratingReviewer = ratingReviewer;
    }

    public Integer getRatingReview() {
        return ratingReview;
    }

    public void setRatingReview(Integer ratingReview) {
        this.ratingReview = ratingReview;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
