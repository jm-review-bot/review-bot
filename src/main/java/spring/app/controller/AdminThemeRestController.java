package spring.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.FixedThemeDto;
import spring.app.exceptions.ProcessInputException;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.mapper.ThemeMapper;
import spring.app.model.FixedTheme;
import spring.app.model.Theme;
import spring.app.service.abstraction.ThemeService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/theme")
public class AdminThemeRestController {

    private ThemeService themeService;
    private ThemeMapper themeMapper;

    public AdminThemeRestController(ThemeService themeService, ThemeMapper themeMapper) {
        this.themeService = themeService;
        this.themeMapper = themeMapper;
    }

    @GetMapping
    public ResponseEntity<List<FixedThemeDto>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemesDto());
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<FixedThemeDto> getThemeById(@PathVariable Long themeId) {
        FixedThemeDto fixedThemeDtoById = themeService.getThemeDtoById(themeId);
        if (fixedThemeDtoById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(fixedThemeDtoById);
    }

    @Validated(CreateGroup.class)
    @PostMapping
    public ResponseEntity<FixedThemeDto> createTheme(@RequestBody @Valid FixedThemeDto fixedThemeDto) {
        FixedTheme fixedTheme = themeMapper.fixedThemeDtoToFixedThemeEntity(fixedThemeDto);
        fixedTheme.setPosition(themeService.getThemeMaxPositionValue() + 1); // автоматическое выстановление позиции
        themeService.addTheme(fixedTheme);
        return ResponseEntity.status(HttpStatus.CREATED).body(themeMapper.fixedThemeEntityToFixedThemeDto(fixedTheme));
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity deleteTheme(@PathVariable Long themeId) {
        themeService.deleteThemeById(themeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Validated(UpdateGroup.class)
    @PutMapping("/{themeId}")
    public ResponseEntity updateTheme(@PathVariable Long themeId, @RequestBody @Valid FixedThemeDto fixedThemeDto) {
        Theme themeById = themeService.getThemeById(themeId);
        if (themeById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        FixedTheme updatedFixedTheme = themeMapper.fixedThemeDtoToFixedThemeEntity(fixedThemeDto);
        updatedFixedTheme.setPosition(themeById.getPosition());
        themeService.updateTheme(updatedFixedTheme);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*
     * Перемещение темы на одну позицию вверх
     *
     * @param themeId - ID перемещаемой темы
     * */
    @PatchMapping("/{themeId}/position/up")
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
     *
     * @param themeId - ID перемещаемой темы
     * */
    @PatchMapping("/{themeId}/position/down")
    public ResponseEntity<String> moveThemePositionDown(@PathVariable String themeId) {
        try {
            themeService.shiftThemePosition(Long.parseLong(themeId), 1);
            return ResponseEntity.ok("Тема перемещена на одну позицию вниз");
        } catch (ProcessInputException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
