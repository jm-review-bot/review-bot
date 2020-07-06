package spring.app.dto;

import spring.app.groups.CreateGroup;
import spring.app.groups.GetGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class QuestionDto {

    @Null(groups = CreateGroup.class)
    @NotNull(groups = GetGroup.class)
    private Long id;

    private String title;

    @NotNull(groups = GetGroup.class)
    private Integer position;

    private String answer;
    private Integer weight;

    public QuestionDto () {}

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public void setPosition (int position) {
        this.position = position;
    }

    public Long getId () {
        return id;
    }

    public String getTitle () {
        return title;
    }

    public int getPosition () {
        return position;
    }


}