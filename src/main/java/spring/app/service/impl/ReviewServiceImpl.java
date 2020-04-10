package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;
import spring.app.service.abstraction.ReviewService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewDao reviewDao;

    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Transactional
    @Override
    public void addReview(Review review) {
        reviewDao.save(review);
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewDao.getById(id);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewDao.getAll();
    }

    @Transactional
    @Override
    public void updateReview(Review review) {
        reviewDao.update(review);
    }

    @Transactional
    @Override
    public void deleteReviewById(Long id) {
        reviewDao.deleteById(id);
    }

    @Transactional
    @Override
    public void updateAllExpiredReviewsByDate(LocalDateTime localDateTime) {
        reviewDao.updateAllExpiredReviewsByDate(localDateTime);
    }

    @Override
    public List<Review> getOpenReviewsByReviewerVkId(Integer vkId, LocalDateTime periodStart, int reviewDuration) {
        return reviewDao.getOpenReviewsByReviewerVkId(vkId, periodStart, reviewDuration);
    }

    @Override
    public List<Review> getOpenReviewsByReviewerVkId(Integer vkId) {
        return reviewDao.getOpenReviewsByReviewerVkId(vkId);
    }

    @Override
    public Review getOpenReviewByStudentVkId(Integer vkId) {
        return reviewDao.getOpenReviewByStudentVkId(vkId);
    }
}
