package spring.app.dto;

import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    private String studentComment;
    private Integer ratingReviewer;
    private Integer ratingReview;

    public FeedbackDto(@Null(groups = CreateGroup.class) @NotNull(groups = UpdateGroup.class) Long id, @NotBlank String reviewerFirstName, @NotBlank String reviewerLastName, @NotBlank String studentFirstName, @NotBlank String studentLastName, String studentComment, Integer ratingReviewer, Integer ratingReview) {
        this.id = id;
        this.reviewerFirstName = reviewerFirstName;
        this.reviewerLastName = reviewerLastName;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentComment = studentComment;
        this.ratingReviewer = ratingReviewer;
        this.ratingReview = ratingReview;
    }
}
