package spring.app.dao.abstraction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dto.QuestionDto;
import spring.app.model.Question;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface QuestionDao extends GenericDao<Long, Question> {

    List<Question> getQuestionsByReviewId(Long reviewId);

    Optional<Question> getQuestionByStudentReviewAnswerId(Long studentReviewAnswerId);

    List<Question> getQuestionsByThemeId(Long themeId);

    Optional<Question> getQuestionByThemeIdAndId(Long themeId, Long questionId);

    Integer getQuestionMinPositionByThemeId(Long themeId);

    Integer getQuestionMaxPositionByThemeId(Long themeId);

    @Transactional(propagation = Propagation.MANDATORY)
    void shiftQuestionsPosition(Long themeId, Integer positionLow, Integer positionHigh, Integer positionShift);

    List<QuestionDto> getAllQuestionDtoByTheme(Long themeId);

    Optional<QuestionDto> getQuestionDtoById(Long id);

    List<QuestionDto> questionsSearch(String searchString);
}
