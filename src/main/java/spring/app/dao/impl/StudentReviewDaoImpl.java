package spring.app.dao.impl;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.model.StudentReview;
import spring.app.model.Theme;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class StudentReviewDaoImpl extends AbstractDao<Long, StudentReview> implements StudentReviewDao {

    public StudentReviewDaoImpl() {
        super(StudentReview.class);
    }

    /**
     * Метод возвращает запись на ревью, если оно еще открыто
     *
     * @param idUser
     */
    public StudentReview getStudentReviewIfAvailableAndOpen(Long idUser) {
        String str = "SELECT sr FROM StudentReview sr JOIN FETCH sr.review srr JOIN FETCH srr.theme INNER JOIN Review r " +
                "ON sr.review.id = r.id WHERE sr.user.id = :id_user AND r.isOpen = true";
        TypedQuery<StudentReview> query = entityManager.createQuery(str, StudentReview.class).setMaxResults(1)
                .setParameter("id_user", idUser);
        return DataAccessUtils.singleResult(query.getResultList());
    }

    /**
     * Метод возвращает количество участников записанных на ревью
     *
     * @param idReview
     */
    public Long getNumberStudentReviewByIdReview(Long idReview) {
        return (Long) entityManager.createQuery("SELECT count (sr) FROM StudentReview sr WHERE sr.review.id = :id_review")
                .setParameter("id_review", idReview).getResultList().get(0);
    }

    /**
     * Метод возвращает список ревью студента по теме
     *
     * @param vkId
     * @param theme
     * @return
     */
    @Override
    public List<StudentReview> getAllStudentReviewsByStudentVkIdAndTheme(Long vkId, Theme theme) {
        return entityManager.createQuery("SELECT sr FROM StudentReview sr " +
                "JOIN FETCH sr.user u JOIN FETCH sr.review r WHERE u.id = :vkId AND r.theme = :theme", StudentReview.class)
                .setParameter("vkId", vkId)
                .setParameter("theme", theme)
                .getResultList();
    }

    @Override
    public StudentReview getStudentReviewsByIdWithFetchReviewUserThemeAndReviewer(Long id) {
        return entityManager.createQuery("SELECT sr FROM StudentReview sr " +
                "JOIN FETCH sr.review r JOIN FETCH sr.user u JOIN FETCH r.theme JOIN FETCH r.user rewiever WHERE sr.id = :srId", StudentReview.class)
                .setParameter("srId", id)
                .getResultList().get(0);

    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteStudentReviewByVkId(Integer vkId) {
        entityManager.flush();
        entityManager.clear();
        entityManager.createQuery("DELETE FROM StudentReview x WHERE x IN (" +
                "SELECT sr FROM StudentReview sr JOIN User u ON u.id = sr.user.id WHERE sr.user.vkId =:id AND sr.review.isOpen = true)")
                .setParameter("id", vkId)
                .executeUpdate();
    }

    @Override
    public StudentReview getStudentReviewByReviewIdAndStudentId(Long reviewId, Long studentId) {
        return entityManager.createQuery("SELECT sr FROM StudentReview sr JOIN FETCH sr.user u JOIN FETCH sr.review r WHERE u.id = :student_id AND r.id = :review_id", StudentReview.class)
                .setParameter("review_id", reviewId)
                .setParameter("student_id", studentId)
                .getSingleResult();
    }

    /**
     * Метод возвращает открытые ревью, на сдачу которого которое записался юзер с
     *
     * @param vkId
     */
    @Override
    public List<StudentReview> getOpenReviewByStudentVkId(Integer vkId) throws NoResultException {
        return entityManager.createQuery(
                "SELECT sr FROM StudentReview sr JOIN FETCH sr.review srr JOIN FETCH srr.theme WHERE srr.isOpen = true AND sr.user.vkId = :vkId", StudentReview.class)
                .setParameter("vkId", vkId)
                .getResultList();
    }

    @Override
    public List<StudentReview> getAllStudentReviewsByReviewId(Long reviewId) {
        return entityManager.createQuery("SELECT sr FROM StudentReview sr WHERE sr.review.id = :review_id")
                .setParameter("review_id", reviewId)
                .getResultList();
    }

    @Override
    public List<StudentReview> getStudentReviewsByStudentId(Long studentId) {
        return entityManager
                .createQuery("SELECT sr FROM StudentReview sr WHERE sr.user.id = :student_id", StudentReview.class)
                .setParameter("student_id", studentId)
                .getResultList();
    }
}
