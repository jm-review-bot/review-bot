package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.QuestionDao;
import spring.app.dto.QuestionDto;
import spring.app.model.Question;
import spring.app.util.SingleResultHelper;

import java.util.List;
import java.util.Optional;

@Repository
public class QuestionDaoImpl extends AbstractDao<Long, Question> implements QuestionDao {

    private final SingleResultHelper<Question> singleResultHelper = new SingleResultHelper<>();
    private final SingleResultHelper<QuestionDto> dtoSingleResultHelper = new SingleResultHelper<>();

    public QuestionDaoImpl() {
        super(Question.class);
    }

    @Override
    public List<Question> getQuestionsByReviewId(Long reviewId) {
        return entityManager.createQuery("SELECT q FROM Question q INNER JOIN Theme t ON q.fixedTheme.id = t.id JOIN Review r on t.id = r.theme.id WHERE r.id = :review_id ORDER BY q.position", Question.class)
                .setParameter("review_id", reviewId)
                .getResultList();
    }

    @Override
    public Optional<Question> getQuestionByStudentReviewAnswerId(Long studentReviewAnswerId) {
        return singleResultHelper.singleResult(entityManager.createQuery("select q FROM Question q JOIN StudentReviewAnswer sra ON sra.question.id = q.id WHERE sra.id = :student_review_answer_id", Question.class)
                .setParameter("student_review_answer_id", studentReviewAnswerId));
    }

    @Override
    public List<Question> getQuestionsByThemeId(Long themeId) {
        return entityManager.createQuery("SELECT q FROM Question q WHERE q.fixedTheme.id = :theme_id ORDER BY q.position", Question.class)
                .setParameter("theme_id", themeId)
                .getResultList();
    }

    @Override
    public Optional<Question> getQuestionByThemeIdAndId(Long themeId, Long questionId) {
        return singleResultHelper.singleResult(entityManager.createQuery("SELECT q FROM Question q WHERE q.fixedTheme.id = :theme_id AND q.id = :question_id", Question.class).setParameter("theme_id", themeId));
    }

    @Override
    public Integer getQuestionMinPositionByThemeId(Long themeId) {
        List<Integer> minPositionInsideList = entityManager.createQuery("SELECT min(q.position) FROM Question q where q.fixedTheme.id = :theme_id", Integer.class)
                .setParameter("theme_id", themeId)
                .getResultList();
        return minPositionInsideList.contains(null) ? 0 : minPositionInsideList.get(0);
    }

    @Override
    public Integer getQuestionMaxPositionByThemeId(Long themeId) {
        List<Integer> maxPositionInsideList = entityManager.createQuery("SELECT max(q.position) FROM Question q where q.fixedTheme.id = :theme_id", Integer.class)
                .setParameter("theme_id", themeId)
                .getResultList();
        return maxPositionInsideList.contains(null) ? 0 : maxPositionInsideList.get(0);
    }

    @Override
    public void shiftQuestionsPosition(Long themeId, Integer positionLow, Integer positionHigh, Integer positionShift) {
        entityManager.createQuery("UPDATE Question q SET q.position = q.position + (:position_shift) WHERE q.fixedTheme.id = :theme_id AND  q.position BETWEEN :position_low AND :position_high")
                .setParameter("theme_id", themeId)
                .setParameter("position_shift", positionShift)
                .setParameter("position_low", positionLow)
                .setParameter("position_high", positionHigh)
                .executeUpdate();
    }

    @Override
    public List<QuestionDto> getAllQuestionDtoByTheme(Long themeId) {
        return entityManager.createQuery("SELECT new spring.app.dto.QuestionDto(q.id, q.question, q.answer, q.position, q.weight) FROM Question q WHERE q.fixedTheme.id = :theme_id", QuestionDto.class)
                .setParameter("theme_id", themeId)
                .getResultList();
    }

    @Override
    public Optional<QuestionDto> getQuestionDtoById(Long id) {
        return dtoSingleResultHelper.singleResult(entityManager.createQuery("SELECT new spring.app.dto.QuestionDto(q.id,q.question,q.answer,q.position,q.weight) FROM Question q WHERE q.id = :id", QuestionDto.class).setParameter("id", id));
    }

    @Override
    public List<QuestionDto> questionsSearch(String searchString) {
        return entityManager.createQuery("SELECT new spring.app.dto.QuestionDto(q.id, q.question, q.answer, q.position, q.weight) FROM Question q WHERE LOWER(q.question) LIKE LOWER(:search)", QuestionDto.class)
                .setParameter("search", "%" + searchString + "%")
                .getResultList();
    }
}