package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.FeedbackDao;
import spring.app.dto.FeedbackDto;
import spring.app.model.Feedback;
import spring.app.service.abstraction.FeedbackService;

import java.util.List;
import java.util.Optional;


@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackDao feedbackDao;

    @Autowired
    public FeedbackServiceImpl(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    @Transactional
    @Override
    public void addFeedback(Feedback feedback) {
        feedbackDao.save(feedback);
    }

    @Override
    public String getStudentCommentByFeedbackId(Long id) {
        return feedbackDao.getStudentCommentByFeedbackId(id);
    }

    @Override
    public Optional<FeedbackDto> getFeedbackDtoById(Long id) {
        return feedbackDao.getFeedbackDtoById(id);
    }

    @Override
    public List<FeedbackDto> getAllFeedbacksDto() {
        return feedbackDao.getAllFeedbacksDto();
    }

    @Override
    public List<Feedback> getFeedbackByStudentReviewId(Long studentReviewId) {
        return feedbackDao.getFeedbackByStudentReviewId(studentReviewId);
    }

    @Transactional
    @Override
    public void removeAll(List<Feedback> feedbacks) {
        feedbackDao.removeAll(feedbacks);
    }

    @Override
    public List<Feedback> getFeedbacksByStudentId(Long studentId) {
        return feedbackDao.getFeedbackByStudentReviewId(studentId);
    }
}
