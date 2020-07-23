package spring.app.dto;

import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class FeedbackDto {
    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Long id;

    @NotBlank
    private String reviewerName;

    @NotBlank
    private String studentName;

    private String studentComment;
    private Integer ratingReviewer;
    private Integer ratingReview;

    public FeedbackDto(@Null(groups = CreateGroup.class) @NotNull(groups = UpdateGroup.class) Long id, @NotBlank String reviewerName, @NotBlank String studentName, String studentComment, Integer ratingReviewer, Integer ratingReview) {
        this.id = id;
        this.reviewerName = reviewerName;
        this.studentName = studentName;
        this.studentComment = studentComment;
        this.ratingReviewer = ratingReviewer;
        this.ratingReview = ratingReview;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentComment() {
        return studentComment;
    }

    public void setStudentComment(String studentComment) {
        this.studentComment = studentComment;
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
}
