package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.model.StudentReview;

@Repository
public class StudentReviewDaoImpl extends AbstractDao<Long, StudentReview> implements StudentReviewDao {

    public StudentReviewDaoImpl() {
        super(StudentReview.class);
    }

}
