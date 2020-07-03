package spring.app.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class ChangeThemePosition {

    ThemeService themeService;

    @Autowired
    public ChangeThemePosition(ThemeService themeService) {
        this.themeService = themeService;

    }

    /*
    * Перемещение темы на одну позицию вверх
    * */

    @PatchMapping("/up")
    public ResponseEntity<String> upThemePosition(@PathVariable String themeId) {
        try {
            themeService.shiftThemePosition(Long.parseLong(themeId), -1);
            return new ResponseEntity<>("Тема перемещена на одну позицию вверх", HttpStatus.OK);
        } catch (ProcessInputException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Перемещение темы на одну позицию вниз
     * */

    @PatchMapping("/down")
    public ResponseEntity<String> downThemePosition(@PathVariable String themeId) {
        try {
            themeService.shiftThemePosition(Long.parseLong(themeId), 1);
            return new ResponseEntity<>("Тема перемещена на одну позицию вниз", HttpStatus.OK);
        } catch (ProcessInputException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
