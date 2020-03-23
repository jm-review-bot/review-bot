package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.StudentReviewAnswerDao;
import spring.app.model.StudentReviewAnswer;

@Repository
public class StudentReviewAnswerDaoImpl extends AbstractDao<Long, StudentReviewAnswer> implements StudentReviewAnswerDao {

    public StudentReviewAnswerDaoImpl() {
        super(StudentReviewAnswer.class);
    }

}
