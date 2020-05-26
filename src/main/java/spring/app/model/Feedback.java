package spring.app.model;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Review review;

    @Column(name = "rating_reviewer")
    private String ratingReviewer;

    @Column(name = "rating_review")
    private String ratingReview;

    @Column(name = "student_comment")
    private String comment;

    public Feedback(Long id, User user, Review review, String ratingReviewer, String ratingReview) {
        this.id = id;
        this.user = user;
        this.review = review;
        this.ratingReviewer = ratingReviewer;
        this.ratingReview = ratingReview;
    }

    public Feedback(Long id, User user, Review review, String ratingReviewer, String ratingReview, String comment) {
        this.id = id;
        this.user = user;
        this.review = review;
        this.ratingReviewer = ratingReviewer;
        this.ratingReview = ratingReview;
        this.comment = comment;
    }

    public Feedback() {
    }

    public Feedback(Long id, User user, Review review) {
        this.id = id;
        this.user = user;
        this.review = review;
    }

    public String getRatingReviewer() {
        return ratingReviewer;
    }

    public void setRatingReviewer(String ratingReviewer) {
        this.ratingReviewer = ratingReviewer;
    }

    public String getRatingReview() {
        return ratingReview;
    }

    public void setRatingReview(String ratingReview) {
        this.ratingReview = ratingReview;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return id.equals(feedback.id) &&
                user.equals(feedback.user) &&
                review.equals(feedback.review) &&
                Objects.equals(ratingReviewer, feedback.ratingReviewer) &&
                Objects.equals(ratingReview, feedback.ratingReview) &&
                Objects.equals(comment, feedback.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, review, ratingReviewer, ratingReview, comment);
    }
}
