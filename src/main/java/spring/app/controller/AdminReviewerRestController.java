package spring.app.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.ReviewerDto;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.mapper.ReviewerMapper;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.UserService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/theme")
public class AdminReviewerRestController {

    private ReviewerMapper reviewerMapper;
    private UserService userService;
    private ReviewService reviewService;

    public AdminReviewerRestController (ReviewerMapper reviewerMapper , UserService userService , ReviewService reviewService) {
        this.reviewerMapper = reviewerMapper;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/{themeId}/reviewer")
    public ResponseEntity<List<ReviewerDto>> getAllReviewers (@PathVariable long themeId) {
        List<ReviewerDto> examiners = userService.getExaminersInThisTheme(themeId);
        return ResponseEntity.ok(examiners);
    }

    @GetMapping("/{themeId}/reviewer/notInThisTheme")
    public ResponseEntity<List<ReviewerDto>> getAllReviewersNotInThisTheme (@PathVariable long themeId) {
       List<ReviewerDto> examiners = userService.getExaminersInNotThisTheme(themeId);
       return ResponseEntity.ok(examiners);
    }

    @Validated(UpdateGroup.class)
    @PostMapping("/{themeId}/reviewer")
    public ResponseEntity<ReviewerDto> create (@PathVariable long themeId ,
                                               @RequestBody @Valid ReviewerDto reviewerDto) {
        userService.addNewReviewer(themeId , reviewerDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewerDto);
    }

    @DeleteMapping("/{themeId}/reviewer/{reviewerId}")
    public ResponseEntity deleteReviewer(@PathVariable long themeId ,
                                         @PathVariable long reviewerId) {
        userService.deleteReviewerFromTheme(themeId , reviewerId);
        return ResponseEntity.noContent().build();
    }
}
