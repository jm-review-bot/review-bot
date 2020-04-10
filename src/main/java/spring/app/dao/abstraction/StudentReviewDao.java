package spring.app.dao.abstraction;

import spring.app.model.StudentReview;

public interface StudentReviewDao extends GenericDao<Long, StudentReview> {

    StudentReview getStudentReviewIfAvailableAndOpen(Long idUser);

    Long getNumberStudentReviewByIdReview(Long idReview);

    void bulkDeleteByUserId(Long id);

    void deleteStudentReviewByVkId(Integer vkId);

    StudentReview getStudentReviewByReviewIdAndStudentId (Long reviewId, Long studentId);
}
