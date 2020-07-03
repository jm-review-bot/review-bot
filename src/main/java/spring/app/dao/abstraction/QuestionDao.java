package spring.app.dao.abstraction;

import spring.app.model.Question;

import java.util.List;

public interface QuestionDao extends GenericDao<Long, Question> {

    List<Question> getQuestionsByReviewId(Long reviewId);

    Question getQuestionByStudentReviewAnswerId(Long studentReviewAnswerId);

    List<Question> getQuestionsByThemeId(Long themeId);

    Question getQuestionByThemeIdAndId(Long themeId, Long questionId);

    Integer getQuestionMinPositionByThemeId(Long themeId);

    Integer getQuestionMaxPositionByThemeId(Long themeId);

    void shiftQuestionsPosition(Long themeId, Integer positionLow, Integer positionHigh, Integer positionShift);
}
