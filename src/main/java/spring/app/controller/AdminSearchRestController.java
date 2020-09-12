package spring.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.app.service.abstraction.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/search")
@Api(value = "Search controller")
public class AdminSearchRestController {

    private final ThemeService themeService;
    private final QuestionService questionService;

    public AdminSearchRestController(ThemeService themeService,
                                     QuestionService questionService) {
        this.themeService = themeService;
        this.questionService = questionService;
    }

    @GetMapping
    @ApiOperation(value = "Search themes")
    public ResponseEntity<List<?>> searchThemes(@ApiParam(value = "Search string", required = false) @RequestParam("searchString") String searchString,
                                                @ApiParam(value = "Entity", required = false) @RequestParam("entity") String entity) {
        switch (entity) {
            case "theme":
                return ResponseEntity.ok(themeService.themesSearch(searchString));
            case "question":
                return ResponseEntity.ok(questionService.questionsSearch(searchString));
            default:
                return ResponseEntity.badRequest().build();
        }
    }
}
