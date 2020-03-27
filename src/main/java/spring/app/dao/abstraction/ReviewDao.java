package spring.app.dao.abstraction;

import spring.app.model.Review;

import java.time.LocalDateTime;

public interface ReviewDao extends GenericDao<Long, Review> {

    void getAllExpiredReviews(LocalDateTime localDateTime);
}
