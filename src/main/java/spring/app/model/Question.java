package spring.app.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import spring.app.listener.QuestionListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(QuestionListener.class)
@Table(name = "question")
@ApiModel(value = "Вопрос")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @ApiModelProperty(notes = "Генерируемый базой данных идентификатор для вопроса")
    private Long id;

    @Column(name = "question")
    @ApiModelProperty(notes = "Тело вопроса")
    private String question;

    @Column(name = "answer", columnDefinition = "TEXT")
    @ApiModelProperty(notes = "Тело ответа")
    private String answer;

    @Column(name = "position")
    @ApiModelProperty(notes = "Позиция вопроса")
    private Integer position;

    @Column(name = "weight")
    @ApiModelProperty(notes = "Вес вопроса")
    private Integer weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    @ApiModelProperty(notes = "Id темы")
    private Theme theme;

    public Question() {
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return id.equals(question1.id) &&
               question.equals(question1.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question);
    }
}
