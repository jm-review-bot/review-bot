package spring.app.service.abstraction;

import spring.app.dto.FeedbackDto;
import spring.app.model.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {

    void addFeedback(Feedback feedback);

    String getStudentCommentByFeedbackId(Long id);

    Optional<FeedbackDto> getFeedbackDtoById(Long id);

    List<FeedbackDto> getAllFeedbacksDto();

    List<Feedback> getFeedbackByStudentReviewId(Long studentReviewId);

    void removeAll(List<Feedback> feedbacks);

    List<Feedback> getFeedbacksByStudentId(Long studentId);
}
