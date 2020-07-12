package spring.app.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.service.abstraction.QuestionService;

@RequestMapping("/admin/theme/{themeId}/question")
@RestController
@Api(value = "Question remove Api: up, down ")
public class QuestionRestController {

    private QuestionService questionService;

    public QuestionRestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PatchMapping("/{questionId}/position/up")
    @ApiOperation(value = "Move theme question position Up", response = ResponseEntity.class)
    public ResponseEntity<?> moveThemeQuestionPositionUp(@PathVariable Long themeId, @PathVariable Long questionId) {
        boolean isChanged = questionService.changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(themeId, questionId, -1);
        return isChanged ? ResponseEntity.ok("Вопрос перемещён на позицию выше") : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{questionId}/position/down")
    @ApiOperation(value = "Move theme question position Down", response = ResponseEntity.class)
    public ResponseEntity<?> moveThemeQuestionPositionDown(@PathVariable Long themeId, @PathVariable Long questionId) {
        boolean isChanged = questionService.changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(themeId, questionId, 1);
        return isChanged ? ResponseEntity.ok("Вопрос перемещён на позицию ниже") : ResponseEntity.badRequest().build();
    }
}
