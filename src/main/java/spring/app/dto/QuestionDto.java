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
@ApiModel(description = "All details about the Question in DTO")
public class QuestionDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(notes = "Question id")
    private Long id;

    @NotBlank
    @ApiModelProperty(notes = "Question")
    private String question;

    @ApiModelProperty(notes = "Answer")
    private String answer;

    @ApiModelProperty(notes = "Question position")
    private Integer position;

    @ApiModelProperty(notes = "Question weight")
    private Integer weight;

    public QuestionDto() {
    }

    public QuestionDto(Long id, String question, String answer, Integer position, Integer weight) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.position = position;
        this.weight = weight;
    }
}
