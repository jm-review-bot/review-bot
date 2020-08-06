package spring.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.ReviewerDto;
import spring.app.groups.UpdateGroup;
import spring.app.model.User;
import spring.app.service.abstraction.UserService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/theme")
public class AdminReviewerRestController {

    private final static Logger log = LoggerFactory.getLogger(AdminReviewerRestController.class);

    private UserService userService;

    public AdminReviewerRestController (UserService userService) {
        this.userService = userService;
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.addNewReviewer(themeId , reviewerDto.getId());
        log.info("Admin(vkId={}) добавил ревьювера (Reviewer=()) в тему (Theme=())" ,
                user.getVkId() , reviewerDto.getId()  + "," + reviewerDto.getFirstName() + "," + reviewerDto.getLastName() , themeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewerDto);
    }

    @DeleteMapping("/{themeId}/reviewer/{reviewerId}")
    public ResponseEntity deleteReviewer(@PathVariable long themeId ,
                                         @PathVariable long reviewerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteReviewerFromTheme(themeId , reviewerId);
        log.info(
                "Admin(vkId={}) удалил ревьювера (reviewerId={}) из (Theme={}))",
                user.getVkId() , reviewerId , themeId);
        return ResponseEntity.noContent().build();
    }
}
