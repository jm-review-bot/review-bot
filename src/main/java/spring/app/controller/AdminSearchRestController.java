package spring.app.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.model.*;
import spring.app.service.abstraction.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/search")
public class AdminSearchRestController {

    private final UserService userService;
    private final ThemeService themeService;
    private final RoleService roleService;
    private final QuestionService questionService;
    private final FeedbackService feedbackService;

    public AdminSearchRestController(UserService userService,
                                     ThemeService themeService,
                                     RoleService roleService,
                                     QuestionService questionService,
                                     FeedbackService feedbackService) {
        this.userService = userService;
        this.themeService = themeService;
        this.roleService = roleService;
        this.questionService = questionService;
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<?> search(@RequestBody String searchString) {
        Map<String, List<?>> searchResults = new HashMap<>();

        List<User> userSearch = userService.usersSearch(searchString);
        List<Theme> themesSearch = themeService.themesSearch(searchString);
        List<Question> questionsSearch = questionService.questionsSearch(searchString);
        List<Feedback> feedbacksSearch = feedbackService.feedbacksSearch(searchString);
        List<Role> rolesSearch = roleService.rolesSearch(searchString);

        searchResults.put("users", userSearch);
        searchResults.put("themes", themesSearch);
        searchResults.put("questions", questionsSearch);
        searchResults.put("feedbacks", feedbacksSearch);
        searchResults.put("roles", rolesSearch);

        return ResponseEntity.ok(searchResults);
    }
}
