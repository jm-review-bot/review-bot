package spring.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.dto.FeedbackDto;
import spring.app.service.abstraction.FeedbackService;

import java.util.List;

@Validated
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
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
    public ResponseEntity<String> getStudentCommentByFeedbackId(@PathVariable Long feedbackId) {
        return ResponseEntity.ok(feedbackService.getStudentCommentByFeedbackId(feedbackId));
    }
}
