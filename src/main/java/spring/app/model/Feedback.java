package spring.app.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "student_feedback")
@ApiModel(value = "Отзыв студента")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @ApiModelProperty(notes = "Генерируемый базой данных идентификатор для отзыва")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @ApiModelProperty(notes = "Id студента")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_review_id")
    @ApiModelProperty(notes = "Id ревью студента")
    private StudentReview studentReview;

    @Column(name = "rating_reviewer")
    @ApiModelProperty(notes = "Рейтинг ревьюера")
    private Integer ratingReviewer;

    @Column(name = "rating_review")
    @ApiModelProperty(notes = "Рейтинг ревью")
    private Integer ratingReview;

    @Column(name = "student_comment")
    @ApiModelProperty(notes = "Коментарии студента")
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
