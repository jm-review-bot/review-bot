package spring.app.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.*;
import spring.app.service.abstraction.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> search(@RequestParam("search") String searchString) {
        Map<String, List<?>> searchResults = new HashMap<>();

        List<ThemeDto> themesSearch = themeService.themesSearch(searchString);
        List<QuestionDto> questionsSearch = questionService.questionsSearch(searchString);

        searchResults.put("themes", themesSearch);
        searchResults.put("questions", questionsSearch);

        return ResponseEntity.ok(searchResults);
    }
}
