package spring.app.service.abstraction;

import spring.app.dto.FeedbackDto;
import spring.app.model.Feedback;

import java.util.List;

public interface FeedbackService {

    void addFeedback(Feedback feedback);

    String getStudentCommentByFeedbackId(Long id);

    FeedbackDto getFeedbackDtoById(Long id);

    List<FeedbackDto> getAllFeedbacksDto();

    List<Feedback> getFeedbackByStudentReviewId(Long studentReviewId);

    void removeAll(List<Feedback> feedbacks);
}