package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.FeedbackDao;
import spring.app.model.Feedback;

import java.util.List;


@Repository
public class FeedbackDaoImpl extends AbstractDao<Long, Feedback> implements FeedbackDao {
    FeedbackDaoImpl() {
        super(Feedback.class);
    }

}
