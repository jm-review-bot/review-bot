package spring.app.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс дто для хранения номера вопроса по теме и вычисленного айдишника следующего отвечающего
 * @author AkiraRokudo on 01.06.2020 in one of sun day
 */
@Getter
@Setter
public class QuestionNumberAnswererIdDto {

    private Integer questionNumber;
    private Long answererId;

    public QuestionNumberAnswererIdDto() {
    }

    public QuestionNumberAnswererIdDto(Integer questionNumber, Long answererId) {
        this.questionNumber = questionNumber;
        this.answererId = answererId;
    }
}
