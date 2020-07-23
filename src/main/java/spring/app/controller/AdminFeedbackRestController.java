package spring.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.dto.FeedbackDto;
import spring.app.dto.ThemeDto;
import spring.app.service.abstraction.FeedbackService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/feedback")
public class AdminFeedbackRestController {
    private FeedbackService feedbackService;

    public AdminFeedbackRestController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacksDto());
    }

    @GetMapping("/{feedbackId}/comment")
    public String getStudentCommentByFeedbackId(@PathVariable Long feedbackId) {
        FeedbackDto feedbackDtoById = feedbackService.getFeedbackDtoById(feedbackId);
        return feedbackDtoById.getStudentComment();
    }
}
