package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.FeedbackDao;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Feedback;
import spring.app.model.Review;
import spring.app.service.abstraction.FeedbackService;


@Service
public class FeedbackServiceImpl implements FeedbackService {
    private FeedbackDao feedbackDao;


    @Autowired
    public FeedbackServiceImpl(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    @Transactional
    @Override
    public void addFeedback(Feedback feedback) {
        feedbackDao.save(feedback);
    }
}
