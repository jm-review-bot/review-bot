package spring.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.dto.FeedbackDto;
import spring.app.service.abstraction.FeedbackService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/admin/feedback")
@Api(value = "Feedback controller")
public class AdminFeedbackRestController {
    private final FeedbackService feedbackService;

    @Autowired
    public AdminFeedbackRestController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @ApiOperation(value = "View a list of all feedbacks")
    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacksDto());
    }

    @ApiOperation(value = "View students comment in feedback")
    @GetMapping("/{feedbackId}/comment")
    public ResponseEntity<Map<String, String>> getStudentCommentByFeedbackId(@ApiParam(value = "Feedback ID", required = true) @PathVariable Long feedbackId) {
        Map<String, String> map = new HashMap<>();
        map.put("comment", feedbackService.getStudentCommentByFeedbackId(feedbackId));
        return ResponseEntity.ok(map);
    }
}
