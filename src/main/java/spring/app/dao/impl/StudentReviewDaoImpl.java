package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.model.StudentReview;

import javax.persistence.TypedQuery;

@Repository
public class StudentReviewDaoImpl extends AbstractDao<Long, StudentReview> implements StudentReviewDao {

    public StudentReviewDaoImpl() {
        super(StudentReview.class);
    }

    /**
     * Метод возвращает запись на ревью, если оно еще открыто
     * @param idUser
     */
    public StudentReview getStudentReviewIfAvailableAndOpen(Long idUser) {
        String str = "SELECT sr FROM StudentReview sr JOIN FETCH sr.review srr JOIN FETCH srr.theme INNER JOIN Review r " +
                "ON sr.review.id = r.id WHERE sr.user.id = :id_user AND r.isOpen = true";
        TypedQuery<StudentReview> query = entityManager.createQuery(str, StudentReview.class)
                .setParameter("id_user", idUser);
        return org.springframework.dao.support.DataAccessUtils.singleResult(query.getResultList());
    }

    /**
     * Метод возвращает количество участников записанных на ревью
     * @param idReview
     */
    public Long getNumberStudentReviewByIdReview(Long idReview) {
        return (Long) entityManager.createQuery("SELECT count (sr) FROM StudentReview sr WHERE sr.review.id = :id_review")
                .setParameter("id_review", idReview).getResultList().get(0);
    }
}
