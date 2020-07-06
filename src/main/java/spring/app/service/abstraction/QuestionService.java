package spring.app.service.abstraction;

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

    void deleteQuestion(Long questionId);

    List<Question> getAllQuestionByThemeId(Long id);
}
