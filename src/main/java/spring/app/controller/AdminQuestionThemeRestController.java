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
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/admin/theme")
@Api(value = "Themes question controller")
public class AdminQuestionThemeRestController {

    private final static Logger logger = LoggerFactory.getLogger(AdminQuestionThemeRestController.class);

    private QuestionService questionService;
    private QuestionMapper questionMapper;
    private ThemeService themeService;

    public AdminQuestionThemeRestController(QuestionService questionService, QuestionMapper questionMapper, ThemeService themeService) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
        this.themeService = themeService;
    }

    @ApiOperation(value = "View a list of all question in theme")
    @GetMapping("/{themeId}/question")
    public ResponseEntity<List<QuestionDto>> getAllQuestionDto(@ApiParam(value = "Theme Id", required = true) @PathVariable Long themeId) {
        return ResponseEntity.ok(questionService.getAllQuestionDtoByTheme(themeId));
    }

    @ApiOperation(value = "Add new question for theme")
    @Validated(CreateGroup.class)
    @PostMapping("/{themeId}/question")
    public ResponseEntity<QuestionDto> createQuestion(@ApiParam(value = "Theme Id", required = true) @PathVariable long themeId,
                                                      @ApiParam(value = "Question object in DTO", required = true) @RequestBody @Valid QuestionDto questionDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Theme theme = themeService.getThemeById(themeId);
        if (!(theme instanceof FixedTheme)) {
            return ResponseEntity.badRequest().build();
        }
        Question question = questionMapper.questionDtoToQuestionEntity(questionDto);
        question.setFixedTheme((FixedTheme) theme);
        questionService.addQuestion(question);
        logger.info(
                "Админ (vkId={}) добавил вопрос(ID={} , Title={}) в тему (ID={} , Title={})",
                user.getVkId() , question.getId() , question.getQuestion() , themeId , theme.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(questionMapper.questionEntityToQuestionDto(question));
    }

    @ApiOperation(value = "Get the question in the theme")
    @GetMapping("/{themeId}/question/{questionId}")
    public ResponseEntity<Optional<QuestionDto>> getQuestionDto(@ApiParam(value = "Question ID", required = true) @PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getQuestionDtoById(questionId));
    }

    @ApiOperation(value = "Update the question")
    @Validated(UpdateGroup.class)
    @PostMapping("/{themeId}/question/{questionId}")
    public ResponseEntity updateQuestion(@ApiParam(value = "Theme ID", required = true) @PathVariable long themeId,
                                         @ApiParam(value = "Question ID", required = true) @PathVariable Long questionId,
                                         @ApiParam(value = "Question object in DTO", required = true) @RequestBody @Valid QuestionDto questionDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Question question = questionService.getQuestionById(questionId);
        Theme theme = themeService.getThemeById(themeId);
        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Question updatedQuestion = questionMapper.questionDtoToQuestionEntity(questionDto);
        updatedQuestion.setFixedTheme(question.getFixedTheme());
        questionService.updateQuestion(updatedQuestion);
        logger.info(
                "Админ (vkId={}) изменил вопрос (ID={} , Title={}) в теме (ID={} , Title={})",
                user.getVkId() ,  questionId , question.getQuestion() , themeId , theme.getTitle());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Delete exist question")
    @DeleteMapping("/{themeId}/question/{questionId}")
    public ResponseEntity deleteQuestion(@ApiParam(value = "Theme ID", required = true) @PathVariable Long themeId,
                                         @ApiParam(value = "Question ID", required = true) @PathVariable Long questionId) {
        Theme theme = themeService.getThemeById(themeId);
        Question question = questionService.getQuestionById(questionId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        questionService.deleteQuestionById(questionId);
        logger.info(
                "Админ (vkId={}) удалил вопрос (ID={} , Title={}) из темы (ID={} , Title={})" ,
                user.getVkId() , questionId , question.getQuestion() , themeId , theme.getTitle());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @ApiOperation(value = "Move the question up in the theme")
    @PatchMapping("/{themeId}/question/{questionId}/position/up")
    public ResponseEntity<?> moveThemeQuestionPositionUp(@ApiParam(value = "Theme ID", required = true) @PathVariable Long themeId,
                                                         @ApiParam(value = "Question ID", required = true) @PathVariable Long questionId) {
        Question question = questionService.getQuestionById(questionId);
        Theme theme = themeService.getThemeById(themeId);
        boolean isChanged = questionService.changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(themeId, questionId, -1);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (isChanged) {
            logger.info(
                    "Админ (vkId={}) переместил вопрос(ID={} , Title={}) на позицию выше в теме (ID={} , Title={})" ,
                    user.getVkId() , questionId , question.getQuestion() , themeId , theme.getTitle()
            );
        }
        return isChanged ? ResponseEntity.ok("Вопрос перемещён на позицию выше") : ResponseEntity.badRequest().build();
    }

    @ApiOperation(value = "Move the question up in the theme")
    @PatchMapping("/{themeId}/question/{questionId}/position/down")
    public ResponseEntity<?> moveThemeQuestionPositionDown(@ApiParam(value = "Theme ID", required = true) @PathVariable Long themeId,
                                                           @ApiParam(value = "Question ID", required = true) @PathVariable Long questionId) {
        Question question = questionService.getQuestionById(questionId);
        Theme theme = themeService.getThemeById(themeId);
        boolean isChanged = questionService.changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(themeId, questionId, 1);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (isChanged) {
            logger.info(
                    "Админ (vkId={}) переместил вопрос(ID={} , Title={}) на позицию ниже в теме (ID={} , Title={})" ,
                    user.getVkId() , questionId , question.getQuestion() , themeId , theme.getTitle()
            );
        }
        return isChanged ? ResponseEntity.ok("Вопрос перемещён на позицию ниже") : ResponseEntity.badRequest().build();
    }
}
