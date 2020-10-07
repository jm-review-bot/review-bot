package spring.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import spring.app.dto.ReviewerDto;
import spring.app.groups.UpdateGroup;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.ThemeService;
import spring.app.service.abstraction.UserService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/theme")
@Api(value = "Reviewer controller")
public class AdminReviewerRestController {

    private final static Logger logger = LoggerFactory.getLogger(AdminReviewerRestController.class);

    private final UserService userService;
    private final ThemeService themeService;

    public AdminReviewerRestController (UserService userService , ThemeService themeService) {
        this.userService = userService;
        this.themeService = themeService;
    }

    @GetMapping("/{themeId}/reviewer")
    @ApiOperation(value = "View list of all reviewer in the theme")
    public ResponseEntity<List<ReviewerDto>> getAllReviewers (@ApiParam(value = "Theme Id", required = true)  @PathVariable long themeId) {
        List<ReviewerDto> examiners = userService.getExaminersInThisTheme(themeId);
        return ResponseEntity.ok(examiners);
    }

    @GetMapping("/{themeId}/reviewer/notInThisTheme")
    @ApiOperation(value = "View list of all 'free' reviewer")
    public ResponseEntity<List<ReviewerDto>> getAllReviewersNotInThisTheme (@ApiParam(value = "Theme Id", required = true) @PathVariable long themeId) {
        List<ReviewerDto> examiners = userService.getExaminersInNotThisTheme(themeId);
        return ResponseEntity.ok(examiners);
    }

    @Validated(UpdateGroup.class)
    @PostMapping("/{themeId}/reviewer")
    @ApiOperation(value = "Add a new reviewer in free theme")
    public ResponseEntity<ReviewerDto> create (@ApiParam(value = "Theme ID", required = true) @PathVariable long themeId ,
                                               @ApiParam(value = "Reviewer model in DTO", required = true) @RequestBody @Valid ReviewerDto reviewerDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User reviewer = userService.getUserById(reviewerDto.getId());
        userService.addNewReviewer(themeId , reviewerDto.getId());
        Theme theme = themeService.getThemeById(themeId);
        logger.info("Админ (vkId={}) добавил ревьювера (ID={} , Reviewer={}) в тему (ID={} , Title={})" ,
                user.getVkId() , reviewer.getId()  , reviewer.getFirstName() + " " + reviewer.getLastName() ,  themeId , theme.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewerDto);
    }

    @DeleteMapping("/{themeId}/reviewer/{reviewerId}")
    @ApiOperation(value = "Remove the reviewer from free theme")
    public ResponseEntity<?> deleteReviewer(@ApiParam(value = "Theme ID", required = true) @PathVariable long themeId ,
                                         @ApiParam(value = "Reviewer ID", required = true) @PathVariable long reviewerId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User reviewer = userService.getUserById(reviewerId);
        Theme theme = themeService.getThemeById(themeId);
        userService.deleteReviewerFromTheme(themeId,reviewerId);
        logger.info(
                "Админ (vkId={}) удалил ревьювера (ID={} , Reviewer={}) из темы(ID={} , Title={})",
                user.getVkId() , reviewerId , reviewer.getFirstName() + " " + reviewer.getLastName() ,  themeId , theme.getTitle());
        return ResponseEntity.noContent().build();
    }
}
