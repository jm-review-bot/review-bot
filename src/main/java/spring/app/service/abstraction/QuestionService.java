package spring.app.service.abstraction;

import spring.app.dto.QuestionDto;
import spring.app.model.Question;

import java.util.List;

public interface QuestionService {

    void addQuestion(Question question);

    Question getQuestionById(Long id);

    List<Question> getAllQuestions();

    void updateQuestion(Question question);

    void deleteQuestionById(Long id);

    List<Question> getQuestionsByReviewId(Long reviewId);

    Question getQuestionByStudentReviewAnswerId(Long studentReviewAnswerId);

    boolean changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(Long themeId, Long questionId, Integer positionChange);

    List<Question> getQuestionsByThemeId(Long themeId);

    List<QuestionDto> getAllQuestionDtoByTheme(Long themeId);

    QuestionDto getQuestionDtoById(Long id);

    void deleteByQuestionTheme(Long themeId, Long questionId);
}
