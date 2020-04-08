package spring.app.dao.abstraction;

import spring.app.model.StudentReview;

public interface StudentReviewDao extends GenericDao<Long, StudentReview> {

    void bulkDeleteByUserId(Long id);

    void deleteStudentReviewByVkId(Integer vkId);

    StudentReview getStudentReviewByReviewIdAndStudentId (Long reviewId, Long studentId);
}
