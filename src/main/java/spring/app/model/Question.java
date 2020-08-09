package spring.app.model;


import lombok.Getter;
import lombok.Setter;
import spring.app.listener.QuestionListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(QuestionListener.class)
@Table(name = "question")
@Getter
@Setter
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
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    public Question() {
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
