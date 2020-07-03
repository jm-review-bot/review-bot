package spring.app.core.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;
import spring.app.service.abstraction.ThemeService;

/*
    Контроллер для изменения позиции темы на одну вверх или вниз
 */

@RestController
@RequestMapping("/admin/theme/{themeId}/position")
public class ChangeThemePosition {

    ThemeService themeService;

    @Autowired
    public ChangeThemePosition(ThemeService themeService) {
        this.themeService = themeService;

    }

/*
    Перемещении темы на одну позицию вверх
 */
    @PatchMapping("/up")
    public ResponseEntity<String> upThemePosition(@PathVariable String themeId) throws ProcessInputException {
        try {
            Theme themeToUp = themeService.getThemeById(Long.parseLong(themeId));       // Тема, которую необходимо переместить
            int currentPosition = themeToUp.getPosition();                              // Текущая позиция перемещаемой темы
            int newPosition = currentPosition - 1;                                      // Новая позиция перемещаемой темы
            Theme themeToDown = themeService.getByPosition(newPosition);                // Тема выше, на место которой будет перемещаться выбранная тема
            themeToUp.setPosition(newPosition);                                         // Устанавливаем новую позицию выбранной теме
            themeToDown.setPosition(currentPosition);                                   // Устанавливаем новую позицию теме, которая была выше
            themeService.updateTheme(themeToUp);                                        // Вносим изменения в тему через ThemeService
            themeService.updateTheme(themeToDown);                                      // Вносим изменения в тему через ThemeService
            return new ResponseEntity<>(
                    "Тема перемещена на позицию выше",
                    HttpStatus.OK
            );
        } catch (EmptyResultDataAccessException exception) {
            return new ResponseEntity<>(
                    "Тема не может быть перемещенена выше, поскольку является первой в списке",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /*
    Перемещении темы на одну позицию вниз
 */
    @PatchMapping("/down")
    public ResponseEntity<String> downThemePosition(@PathVariable String themeId) throws ProcessInputException {
        try {
            Theme themeToDown = themeService.getThemeById(Long.parseLong(themeId));       // Тема, которую необходимо переместить
            int currentPosition = themeToDown.getPosition();                              // Текущая позиция перемещаемой темы
            int newPosition = currentPosition + 1;                                        // Новая позиция перемещаемой темы
            Theme themeToUp = themeService.getByPosition(newPosition);                    // Тема ниже, на место которой будет перемещаться выбранная тема
            themeToDown.setPosition(newPosition);                                         // Устанавливаем новую позицию выбранной теме
            themeToUp.setPosition(currentPosition);                                       // Устанавливаем новую позицию теме, которая была ниже
            themeService.updateTheme(themeToDown);                                        // Вносим изменения в тему через ThemeService
            themeService.updateTheme(themeToUp);                                          // Вносим изменения в тему через ThemeService
            return new ResponseEntity<>(
                    "Тема перемещена на позицию ниже",
                    HttpStatus.OK
            );
        } catch (EmptyResultDataAccessException exception) {
            return new ResponseEntity<>(
                    "Тема не может быть перемещенена выше, поскольку является первой в списке",
                    HttpStatus.BAD_REQUEST);
        }
    }
}
