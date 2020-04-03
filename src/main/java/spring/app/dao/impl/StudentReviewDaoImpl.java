package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.model.StudentReview;

import javax.persistence.TypedQuery;

@Repository
public class StudentReviewDaoImpl extends AbstractDao<Long, StudentReview> implements StudentReviewDao {

    private static final String ID_ID_USER_AND_R_IS_OPEN_TRUE = "SELECT sr FROM StudentReview sr JOIN FETCH sr.review srr JOIN FETCH srr.theme INNER JOIN Review r ON sr.review.id = r.id WHERE sr.user.id = :id_user AND r.isOpen = true";

    public StudentReviewDaoImpl() {
        super(StudentReview.class);
    }

    public StudentReview getStudentReviewIfAvailableAndOpen(Long idUser) {
        TypedQuery<StudentReview> query = entityManager.createQuery(ID_ID_USER_AND_R_IS_OPEN_TRUE, StudentReview.class)
        .setParameter("id_user", idUser);
        return org.springframework.dao.support.DataAccessUtils.singleResult(query.getResultList());
    }

}
