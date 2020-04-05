package spring.app.dao.abstraction;

import spring.app.model.Question;

import java.util.List;

public interface QuestionDao extends GenericDao<Long, Question> {

    List<Question> getQuestionsByReviewId(Long reviewId);
}
