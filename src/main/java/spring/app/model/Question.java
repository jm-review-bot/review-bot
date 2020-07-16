package spring.app.model;


import spring.app.listener.QuestionListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(QuestionListener.class)
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @Column(name = "position")
    private Integer position;

    @Column(name = "weight")
    private Integer weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_theme_id", nullable = false)
    private FixedTheme fixedTheme;

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

    public FixedTheme getFixedTheme() {
        return fixedTheme;
    }

    public void setFixedTheme(FixedTheme fixedTheme) {
        this.fixedTheme = fixedTheme;
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
