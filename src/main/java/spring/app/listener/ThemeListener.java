package spring.app.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.app.model.Question;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.service.abstraction.QuestionService;
import spring.app.service.abstraction.ReviewService;

import javax.persistence.PreRemove;
import java.util.List;

@Component
public class ThemeListener {

    private static QuestionService questionService;
    private static ReviewService reviewService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Autowired
    public void setReviewService (ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreRemove
    private void removeRelatedEntities(Theme theme) {
        Long themeId = theme.getId();
        List<Question> questions = questionService.getQuestionsByThemeId(themeId);
        questionService.removeQuestions(questions);
        List<Review> reviews = reviewService.getReviewsByThemeId(themeId);
        reviewService.removeAll(reviews);
    }
}
