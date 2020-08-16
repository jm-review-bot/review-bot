package spring.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.app.service.abstraction.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/search")
public class AdminSearchRestController {

    private final ThemeService themeService;
    private final QuestionService questionService;

    public AdminSearchRestController(ThemeService themeService,
                                     QuestionService questionService) {
        this.themeService = themeService;
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<?>> searchThemes(@RequestParam("searchString") String searchString,
                                                @RequestParam("entity") String entity) {
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
