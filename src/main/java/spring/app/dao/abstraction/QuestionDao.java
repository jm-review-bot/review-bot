package spring.app.dao.abstraction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dto.QuestionDto;
import spring.app.model.Question;

import java.util.List;

public interface QuestionDao extends GenericDao<Long, Question> {

    List<Question> getQuestionsByReviewId(Long reviewId);

    Question getQuestionByStudentReviewAnswerId(Long studentReviewAnswerId);

    List<Question> getQuestionsByThemeId(Long themeId);

    Question getQuestionByThemeIdAndId(Long themeId, Long questionId);

    Integer getQuestionMinPositionByThemeId(Long themeId);

    Integer getQuestionMaxPositionByThemeId(Long themeId);

    @Transactional(propagation = Propagation.MANDATORY)
    void shiftQuestionsPosition(Long themeId, Integer positionLow, Integer positionHigh, Integer positionShift);

    List<QuestionDto> getAllQuestionDtoByTheme(Long themeId);

    QuestionDto getQuestionDtoById(Long id);

    @Transactional(propagation = Propagation.MANDATORY)
    void deleteByQuestionTheme(Long questionId);
}
