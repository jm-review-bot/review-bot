package spring.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.FixedThemeDto;
import spring.app.dto.FreeThemeDto;
import spring.app.dto.ThemeDto;
import spring.app.exceptions.ProcessInputException;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.mapper.ThemeMapper;
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
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemesDto());
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<ThemeDto> getThemeById(@PathVariable Long themeId) {
        ThemeDto themeDto = themeService.getThemeDtoById(themeId);
        if (themeDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(themeDto);
    }

    @Validated(CreateGroup.class)
    @PostMapping
    public ResponseEntity<FixedThemeDto> createTheme(@RequestBody @Valid ThemeDto themeDto) {
        Theme theme = null;
        if (themeDto instanceof FixedThemeDto) {
            theme = themeMapper.fixedThemeDtoToFixedThemeEntity(themeDto);
        } else if (themeDto instanceof FreeThemeDto) {
            theme = themeMapper.freeThemeDtoToFreeThemeEntity(themeDto);
        }
        themeService.addTheme(theme);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity deleteTheme(@PathVariable Long themeId) {
        themeService.deleteThemeById(themeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Validated(UpdateGroup.class)
    @PutMapping("/{themeId}")
    public ResponseEntity updateTheme(@PathVariable Long themeId, @RequestBody @Valid ThemeDto themeDto) {
        if (themeService.getThemeById(themeId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Theme updatedTheme = null;
        if (themeDto instanceof FixedThemeDto) {
            updatedTheme = themeMapper.fixedThemeDtoToFixedThemeEntity(themeDto);
        } else if (themeDto instanceof FreeThemeDto) {
            updatedTheme = themeMapper.freeThemeDtoToFreeThemeEntity(themeDto);
        }
        themeService.updateTheme(updatedTheme);
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
