package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.model.StudentReview;

@Repository
public class StudentReviewDaoImpl extends AbstractDao<Long, StudentReview> implements StudentReviewDao {

    public StudentReviewDaoImpl() {
        super(StudentReview.class);
    }

    @Override
    @Transactional(propagation= Propagation.MANDATORY)
    public void bulkDeleteByUserId(Long id) {
        // Write all pending changes to the DB
        entityManager.flush();
        // Remove all entities from the persistence context
        entityManager.clear();
        entityManager.createQuery("DELETE FROM StudentReview WHERE user.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    @Transactional(propagation= Propagation.MANDATORY)
    public void deleteStudentReviewByVkId(Integer vkId) {
        entityManager.flush();
        entityManager.clear();
        entityManager.createQuery("DELETE FROM StudentReview x WHERE x IN (" +
                "SELECT sr FROM StudentReview sr JOIN User u ON u.id = sr.user.id WHERE sr.user.vkId =:id)")
                .setParameter("id", vkId)
                .executeUpdate();
    }

    @Override
    public StudentReview getStudentReviewByReviewIdAndStudentId (Long reviewId, Long studentId) {
       return entityManager.createQuery("SELECT sr FROM StudentReview sr JOIN FETCH sr.user u JOIN FETCH sr.review r WHERE u.id = :student_id AND r.id = :review_id", StudentReview.class)
               .setParameter("review_id", reviewId)
               .setParameter("student_id", studentId)
               .getSingleResult();
    }
}
