package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.QuestionDao;
import spring.app.dto.QuestionDto;
import spring.app.model.Question;

import java.util.List;

@Repository
public class QuestionDaoImpl extends AbstractDao<Long, Question> implements QuestionDao {

    public QuestionDaoImpl() {
        super(Question.class);
    }

    @Override
    public List<Question> getQuestionsByReviewId(Long reviewId) {
        return (List<Question>) entityManager.createNativeQuery("select q.* FROM question q INNER JOIN theme t on q.theme_id = t.id JOIN review r on t.id = r.theme_id WHERE r.id = :review_id ORDER BY q.position", Question.class)
                .setParameter("review_id", reviewId)
                .getResultList();
    }

    @Override
    public Question getQuestionByStudentReviewAnswerId(Long studentReviewAnswerId) {
        return entityManager.createQuery("select q FROM Question q JOIN StudentReviewAnswer sra ON sra.question.id = q.id WHERE sra.id = :student_review_answer_id", Question.class)
                .setParameter("student_review_answer_id", studentReviewAnswerId)
                .getSingleResult();
    }

    @Override
    public List<Question> getQuestionsByThemeId(Long themeId) {
        return entityManager.createQuery("SELECT q FROM Question q WHERE q.theme.id = :theme_id ORDER BY q.position", Question.class)
                .setParameter("theme_id", themeId)
                .getResultList();
    }

    @Override
    public Question getQuestionByThemeIdAndId(Long themeId, Long questionId) {
        List<Question> resultList = entityManager.createQuery("SELECT q FROM Question q WHERE q.theme.id = :theme_id AND q.id = :question_id", Question.class)
                .setParameter("theme_id", themeId)
                .setParameter("question_id", questionId)
                .getResultList();
        return resultList.size() != 0 ? resultList.get(0) : null;
    }

    @Override
    public Integer getQuestionMinPositionByThemeId(Long themeId) {
        List<Integer> minPositionInsideList = entityManager.createQuery("SELECT min(q.position) FROM Question q WHERE q.theme.id = :theme_id", Integer.class)
                .setParameter("theme_id", themeId)
                .getResultList();
        return minPositionInsideList.contains(null) ? 0 : minPositionInsideList.get(0);
    }

    @Override
    public Integer getQuestionMaxPositionByThemeId(Long themeId) {
        List<Integer> maxPositionInsideList = entityManager.createQuery("SELECT max(q.position) FROM Question q WHERE q.theme.id = :theme_id", Integer.class)
                .setParameter("theme_id", themeId)
                .getResultList();
        return maxPositionInsideList.contains(null) ? 0 : maxPositionInsideList.get(0);
    }

    @Override
    public void shiftQuestionsPosition(Long themeId, Integer positionLow, Integer positionHigh, Integer positionShift) {
        entityManager.createQuery("UPDATE Question q SET q.position = q.position + (:position_shift) WHERE q.theme.id = :theme_id AND  q.position BETWEEN :position_low AND :position_high")
                .setParameter("theme_id", themeId)
                .setParameter("position_shift", positionShift)
                .setParameter("position_low", positionLow)
                .setParameter("position_high", positionHigh)
                .executeUpdate();
    }

    @Override
    public List<QuestionDto> getAllQuestionDtoByTheme(Long themeId) {
        return entityManager.createQuery("SELECT new spring.app.dto.QuestionDto(q.id, q.question, q.answer, q.position, q.weight) FROM Question q WHERE q.theme.id = :theme_id", QuestionDto.class)
                .setParameter("theme_id", themeId)
                .getResultList();
    }

    @Override
    public QuestionDto getQuestionDtoById(Long id) {
        List<QuestionDto> list = entityManager.createQuery("SELECT new spring.app.dto.QuestionDto(q.id,q.question,q.answer,q.position,q.weight) FROM Question q WHERE q.id = :id", QuestionDto.class)
                .setParameter("id", id)
                .getResultList();
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public void deleteByQuestionTheme(Long themeId, Long questionId) {
        List<Question> list = entityManager.createQuery("DELETE FROM Question q WHERE q.id = :question_id AND q.theme.id = :theme_id", Question.class)
                .setParameter("question_id", questionId)
                .setParameter("theme_id", themeId)
                .getResultList();
        Question question = list.size() > 0 ? list.get(0) : null;
        entityManager.remove(question);
    }
}