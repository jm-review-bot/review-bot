package spring.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.QuestionDto;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.mapper.QuestionMapper;
import spring.app.model.FixedTheme;
import spring.app.model.Question;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.QuestionService;
import spring.app.service.abstraction.ThemeService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/theme")
public class AdminQuestionThemeRestController {

    private final static Logger log = LoggerFactory.getLogger(AdminQuestionThemeRestController.class);

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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Theme theme = themeService.getThemeById(themeId);
        if (!(theme instanceof FixedTheme)) {
            return ResponseEntity.badRequest().build();
        }
        Question question = questionMapper.questionDtoToQuestionEntity(questionDto);
        question.setFixedTheme((FixedTheme) theme);
        questionService.addQuestion(question);
        log.info(
                "Админ (vkId={}) добавил вопрос(ID={} , Title={}) в тему (ID={} , Title={})",
                user.getVkId() , questionDto.getId() , questionDto.getQuestion() , themeId , theme.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(questionMapper.questionEntityToQuestionDto(question));
    }

    @GetMapping("/{themeId}/question/{questionId}")
    public ResponseEntity<QuestionDto> getQuestionDto(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getQuestionDtoById(questionId));
    }

    @Validated(UpdateGroup.class)
    @PostMapping("/{themeId}/question/{questionId}")
    public ResponseEntity updateQuestion(@PathVariable long themeId,
                                         @PathVariable Long questionId,
                                         @RequestBody @Valid QuestionDto questionDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Question question = questionService.getQuestionById(questionId);
        Theme theme = themeService.getThemeById(themeId);
        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Question updatedQuestion = questionMapper.questionDtoToQuestionEntity(questionDto);
        updatedQuestion.setFixedTheme(question.getFixedTheme());
        questionService.updateQuestion(updatedQuestion);
        log.info(
                "Админ (vkId={}) изменил вопрос (ID={} , Title={}) в теме (ID={} , Title={})",
                user.getVkId() ,  question.getId() , question.getQuestion() , theme.getId() , theme.getTitle());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{themeId}/question/{questionId}")
    public ResponseEntity deleteQuestion(@PathVariable Long themeId,
                                         @PathVariable Long questionId) {
        Theme theme = themeService.getThemeById(themeId);
        Question question = questionService.getQuestionById(questionId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        questionService.deleteQuestionById(questionId);
        log.info(
                "Админ (vkId={}) удалил вопрос (ID={} , Title={}) из темы (ID={} , Title={})" ,
                user.getVkId() , question.getId() , question.getQuestion() , themeId , theme.getTitle());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{themeId}/question/{questionId}/position/up")
    public ResponseEntity<?> moveThemeQuestionPositionUp(@PathVariable Long themeId, @PathVariable Long questionId) {
        boolean isChanged = questionService.changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(themeId, questionId, -1);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (isChanged) {
            log.info(
                    "Админ (vkId={}) переместил вопрос(ID={}) на позицию выше" ,
                    user.getVkId() , questionId
            );
        }
        return isChanged ? ResponseEntity.ok("Вопрос перемещён на позицию выше") : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{themeId}/question/{questionId}/position/down")
    public ResponseEntity<?> moveThemeQuestionPositionDown(@PathVariable Long themeId, @PathVariable Long questionId) {
        boolean isChanged = questionService.changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(themeId, questionId, 1);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (isChanged) {
            log.info(
                    "Админ (vkId={}) переместил вопрос(ID={}) на позицию ниже" ,
                    user.getVkId() , questionId
            );
        }
        return isChanged ? ResponseEntity.ok("Вопрос перемещён на позицию ниже") : ResponseEntity.badRequest().build();
    }
}
