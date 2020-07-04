package spring.app.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.exceptions.ProcessInputException;
import spring.app.service.abstraction.ThemeService;

/*
*    Контроллер для изменения позиции темы на одну вверх или вниз
* */

@RestController
@RequestMapping("/admin/theme/{themeId}/position")
public class ThemePositionController {

    @Autowired
    ThemeService themeService;

    /*
    * Перемещение темы на одну позицию вверх
    * */
    @PatchMapping("/up")
    public ResponseEntity<String> moveThemePositionUp (@PathVariable String themeId) {
        try {
            themeService.shiftThemePosition(Long.parseLong(themeId), -1);
            return ResponseEntity.ok("Тема перемещена на одну позицию вверх");
        } catch (ProcessInputException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /*
     * Перемещение темы на одну позицию вниз
     * */
    @PatchMapping("/down")
    public ResponseEntity<String> moveThemePositionDown(@PathVariable String themeId) {
        try {
            themeService.shiftThemePosition(Long.parseLong(themeId), 1);
            return ResponseEntity.ok("Тема перемещена на одну позицию вниз");
        } catch (ProcessInputException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
