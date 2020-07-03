package spring.app.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.service.abstraction.QuestionService;

@RequestMapping("/admin/theme/{themeId}/question/{questionId}/position/")
@RestController
public class QuestionPositionController {

    @Autowired
    private QuestionService questionService;

    @PatchMapping("up")
    public ResponseEntity<?> moveThemeQuestionPositionUp(@PathVariable Long themeId, @PathVariable Long questionId) {
        questionService.changeQuestionPositionByThemeIdAndPositionId(themeId, questionId, -1);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("down")
    public ResponseEntity<?> moveThemeQuestionPositionDown(@PathVariable Long themeId, @PathVariable Long questionId) {
        questionService.changeQuestionPositionByThemeIdAndPositionId(themeId, questionId, 1);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
