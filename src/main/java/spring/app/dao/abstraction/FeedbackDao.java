package spring.app.dao.abstraction;

import spring.app.dto.FeedbackDto;
import spring.app.model.Feedback;

import java.util.List;

public interface FeedbackDao extends GenericDao<Long, Feedback> {

    String getStudentCommentByFeedbackId(Long id);

    FeedbackDto getFeedbackDtoById(Long id);

    List<FeedbackDto> getAllFeedbacksDto();

    List<Feedback> getFeedbackByStudentReviewId(Long studentReviewId);

    List<Feedback> getFeedbacksByStudentId(Long studentId);
}
