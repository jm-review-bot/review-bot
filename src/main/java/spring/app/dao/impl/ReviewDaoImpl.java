package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.model.Review;

@Repository
public class ReviewDaoImpl extends AbstractDao<Long, Review> implements ReviewDao {

    public ReviewDaoImpl() {
        super(Review.class);
    }

}
