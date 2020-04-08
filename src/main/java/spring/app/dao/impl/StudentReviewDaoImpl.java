package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.model.StudentReview;

@Repository
public class StudentReviewDaoImpl extends AbstractDao<Long, StudentReview> implements StudentReviewDao {

    public StudentReviewDaoImpl() {
        super(StudentReview.class);
    }

    @Override
    public StudentReview getStudentReviewByReviewIdAndStudentId (Long reviewId, Long studentId) {
       return entityManager.createQuery("SELECT sr FROM StudentReview sr JOIN FETCH sr.user u JOIN FETCH sr.review r WHERE u.id = :student_id AND r.id = :review_id", StudentReview.class)
               .setParameter("review_id", reviewId)
               .setParameter("student_id", studentId)
               .getSingleResult();
    }
}
