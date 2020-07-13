package spring.app.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "student_review_answer")
@ApiModel(value = "Ответы студента")
public class StudentReviewAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    @ApiModelProperty(notes = "Генерируемый базой данных идентификатор для ответов")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_review_id")
    @ApiModelProperty(notes = "Id ревью студента")
    private StudentReview studentReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(notes = "Id вопроса")
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "is_right")
    @ApiModelProperty(notes = "Корректность ответа")
    private Boolean isRight;

    public StudentReviewAnswer() {
    }

    public StudentReviewAnswer(StudentReview studentReview, Question question, Boolean isRight) {
        this.studentReview = studentReview;
        this.question = question;
        this.isRight = isRight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentReview getStudentReview() {
        return studentReview;
    }

    public void setStudentReview(StudentReview studentReview) {
        this.studentReview = studentReview;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentReviewAnswer that = (StudentReviewAnswer) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
