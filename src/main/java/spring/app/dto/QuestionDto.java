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
public class QuestionDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Long id;

    @NotBlank
    private String question;

    private String answer;
    private Integer position;
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
