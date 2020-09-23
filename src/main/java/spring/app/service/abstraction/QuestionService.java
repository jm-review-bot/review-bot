package spring.app.service.abstraction;

import spring.app.dto.QuestionDto;
import spring.app.model.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    void addQuestion(Question question);

    Question getQuestionById(Long id);

    List<Question> getAllQuestions();

    void updateQuestion(Question question);

    void deleteQuestionById(Long id);

    void removeAll(List<Question> questions);

    List<Question> getQuestionsByReviewId(Long reviewId);

    Optional<Question> getQuestionByStudentReviewAnswerId(Long studentReviewAnswerId);

    boolean changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(Long themeId, Long questionId, Integer positionChange);

    List<Question> getQuestionsByThemeId(Long themeId);

    List<QuestionDto> getAllQuestionDtoByTheme(Long themeId);

    Optional<QuestionDto> getQuestionDtoById(Long id);

    List<QuestionDto> questionsSearch(String searchString);
}
