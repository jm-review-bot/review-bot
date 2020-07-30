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
    private String reviewerFirstName;

    @NotBlank
    private String reviewerLastName;

    @NotBlank
    private String studentFirstName;

    @NotBlank
    private String studentLastName;

    private Integer ratingReviewer;
    private Integer ratingReview;

    public FeedbackDto(@Null(groups = CreateGroup.class) @NotNull(groups = UpdateGroup.class) Long id, @NotBlank String reviewerFirstName, @NotBlank String reviewerLastName, @NotBlank String studentFirstName, @NotBlank String studentLastName, String studentComment, Integer ratingReviewer, Integer ratingReview) {
        this.id = id;
        this.reviewerFirstName = reviewerFirstName;
        this.reviewerLastName = reviewerLastName;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.ratingReviewer = ratingReviewer;
        this.ratingReview = ratingReview;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewerFirstName() {
        return reviewerFirstName;
    }

    public void setReviewerFirstName(String reviewerFirstName) {
        this.reviewerFirstName = reviewerFirstName;
    }

    public String getReviewerLastName() {
        return reviewerLastName;
    }

    public void setReviewerLastName(String reviewerLastName) {
        this.reviewerLastName = reviewerLastName;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
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
