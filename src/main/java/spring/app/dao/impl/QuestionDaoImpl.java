package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.QuestionDao;
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
    public List<Question> getAllQuestionByThemeId(Long id) {
        return (List<Question>) entityManager.createNativeQuery("select q.* FROM question q WHERE q.theme_id = :id")
                .setParameter("id" , id)
                .getResultList();
    }

    @Override
    public void deleteQuestionByThemeId(Long themeId , Long questionId) {
        entityManager.createQuery("DELETE FROM question q WHERE q.theme_id = :themeId and q.id = :questionId")
                .setParameter("themeId" , themeId)
                .setParameter("questionId" , questionId)
                .executeUpdate();
    }

    @Override
    public Question getQuestionByStudentReviewAnswerId(Long studentReviewAnswerId) {
        return entityManager.createQuery("select q FROM Question q JOIN StudentReviewAnswer sra ON sra.question.id = q.id WHERE sra.id = :student_review_answer_id", Question.class)
                .setParameter("student_review_answer_id", studentReviewAnswerId)
                .getSingleResult();
    }
}