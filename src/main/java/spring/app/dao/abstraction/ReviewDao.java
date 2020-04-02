package spring.app.dao.abstraction;

import spring.app.model.Review;
import spring.app.model.Theme;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewDao extends GenericDao<Long, Review> {

    void updateAllExpiredReviewsBy(LocalDateTime localDateTime);

    List<Review> getAllReviewsByTheme(Theme theme);
}
