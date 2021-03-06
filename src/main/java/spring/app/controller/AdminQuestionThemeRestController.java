package spring.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.QuestionDto;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.mapper.QuestionMapper;
import spring.app.model.Question;
import spring.app.model.Theme;
import spring.app.service.abstraction.QuestionService;
import spring.app.service.abstraction.ThemeService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/theme")
public class AdminQuestionThemeRestController {

    private QuestionService questionService;
    private QuestionMapper questionMapper;
    private ThemeService themeService;

    public AdminQuestionThemeRestController(QuestionService questionService, QuestionMapper questionMapper, ThemeService themeService) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
        this.themeService = themeService;
    }

    @GetMapping("/{themeId}/question")
    public ResponseEntity<List<QuestionDto>> getAllQuestionDto(@PathVariable Long themeId) {
        return ResponseEntity.ok(questionService.getAllQuestionDtoByTheme(themeId));
    }

    @Validated(CreateGroup.class)
    @PostMapping("/{themeId}/question")
    public ResponseEntity<QuestionDto> createQuestion(@PathVariable long themeId,
                                                      @RequestBody @Valid QuestionDto questionDto) {
        Theme theme = themeService.getThemeById(themeId);
        Question question = questionMapper.questionDtoToQuestionEntity(questionDto);
        question.setTheme(theme);
        questionService.addQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(questionMapper.questionEntityToQuestionDto(question));
    }

    @GetMapping("/{themeId}/question/{questionId}")
    public ResponseEntity<QuestionDto> getQuestionDto(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getQuestionDtoById(questionId));
    }

    @Validated(UpdateGroup.class)
    @PostMapping("/{themeId}/question/{questionId}")
    public ResponseEntity updateQuestion(@PathVariable Long questionId,
                                         @RequestBody @Valid QuestionDto questionDto) {
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Question updatedQuestion = questionMapper.questionDtoToQuestionEntity(questionDto);
        updatedQuestion.setTheme(question.getTheme());
        questionService.updateQuestion(updatedQuestion);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{themeId}/question/{questionId}")
    public ResponseEntity deleteQuestion(@PathVariable Long themeId,
                                         @PathVariable Long questionId) {
        questionService.deleteQuestionById(questionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{themeId}/question/{questionId}/position/up")
    public ResponseEntity<?> moveThemeQuestionPositionUp(@PathVariable Long themeId, @PathVariable Long questionId) {
        boolean isChanged = questionService.changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(themeId, questionId, -1);
        return isChanged ? ResponseEntity.ok("Вопрос перемещён на позицию выше") : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{themeId}/question/{questionId}/position/down")
    public ResponseEntity<?> moveThemeQuestionPositionDown(@PathVariable Long themeId, @PathVariable Long questionId) {
        boolean isChanged = questionService.changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(themeId, questionId, 1);
        return isChanged ? ResponseEntity.ok("Вопрос перемещён на позицию ниже") : ResponseEntity.badRequest().build();
    }
}
