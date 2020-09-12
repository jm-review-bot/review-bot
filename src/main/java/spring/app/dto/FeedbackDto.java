package spring.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "All details about the Feedback in DTO")
public class FeedbackDto {
    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(notes = "Feedback id")
    private Long id;

    @NotBlank
    @ApiModelProperty(notes = "Reviewer first name")
    private String reviewerFirstName;

    @NotBlank
    @ApiModelProperty(notes = "Reviewer last name")
    private String reviewerLastName;

    @NotBlank
    @ApiModelProperty(notes = "Student first name")
    private String studentFirstName;

    @NotBlank
    @ApiModelProperty(notes = "Student last name")
    private String studentLastName;

    @ApiModelProperty(notes = "Student comment")
    private String studentComment;
    @ApiModelProperty(notes = "Reviewer's rating")
    private Integer ratingReviewer;
    @ApiModelProperty(notes = "Review rating")
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
