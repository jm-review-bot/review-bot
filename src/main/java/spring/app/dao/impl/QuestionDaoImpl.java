package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.QuestionDao;
import spring.app.model.Question;

@Repository
public class QuestionDaoImpl extends AbstractDao<Long, Question> implements QuestionDao {

    public QuestionDaoImpl() {
        super(Question.class);
    }

}
