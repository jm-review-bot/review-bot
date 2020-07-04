package spring.app.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.QuestionDto;
import spring.app.mappers.QuestionMapper;
import spring.app.model.Question;
import spring.app.model.Theme;
import spring.app.service.abstraction.QuestionService;
import spring.app.service.abstraction.ThemeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/theme")
@Validated
public class QuestionThemeController {

    private QuestionService questionService;
    private QuestionMapper  questionMapper;
    private ThemeService themeService;

    @Autowired
    public QuestionThemeController (QuestionService questionService ,
                                    QuestionMapper questionMapper ,
                                    ThemeService themeService) {
        this.themeService = themeService;
        this.questionService = questionService;
        this.questionMapper = questionMapper;
    }

    @GetMapping("/{themeId}/question")
    public @ResponseBody List<QuestionDto> getAll (@PathVariable("themeId") long themeId) {
        return questionService.getAllQuestionByThemeId(themeId)
                .stream().map(q -> questionMapper.toDto(q))
                .collect(Collectors.toList());
    }

    @PostMapping("/{themeId}/question")
    public ResponseEntity<QuestionDto> create (@PathVariable("themeId") long themeId ,
                                               @Validated @RequestBody QuestionDto questionDto) {
        Theme theme = themeService.getThemeById(themeId);
        Question question = questionMapper.toEntity(questionDto);
        question.setTheme(theme);
        questionService.addQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(questionMapper.toDto(question));
    }

    @DeleteMapping("/{themeId}/question/{questionId}")
    public ResponseEntity removeQuestion (@PathVariable("themeId") long themeId ,
                                          @PathVariable("questionId") long questionId) {
        questionService.deleteQuestionByThemeId(themeId , questionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
