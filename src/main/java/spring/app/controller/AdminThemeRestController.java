package spring.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@Api(value = "Admin theme crud operation")
public class AdminThemeRestController {

    private ThemeService themeService;
    private ThemeMapper themeMapper;

    public AdminThemeRestController(ThemeService themeService, ThemeMapper themeMapper) {
        this.themeService = themeService;
        this.themeMapper = themeMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get all", response = ResponseEntity.class)
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemesDto());
    }

    @GetMapping("/{themeId}")
    @ApiOperation(value = "Get theme by Id", response = ResponseEntity.class)
    public ResponseEntity<ThemeDto> getThemeById(@PathVariable Long themeId) {
        ThemeDto themeDtoById = themeService.getThemeDtoById(themeId);
        if (themeDtoById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(themeDtoById);
    }

    @Validated(CreateGroup.class)
    @PostMapping
    @ApiOperation(value = "Create theme", response = ResponseEntity.class)
    public ResponseEntity<ThemeDto> createTheme(@RequestBody @Valid ThemeDto themeDto) {
        Theme theme = themeMapper.themeDtoToThemeEntity(themeDto);
        theme.setPosition(themeService.getThemeMaxPositionValue() + 1); // автоматическое выстановление позиции
        themeService.addTheme(theme);
        return ResponseEntity.status(HttpStatus.CREATED).body(themeMapper.themeEntityToThemeDto(theme));
    }

    @DeleteMapping("/{themeId}")
    @ApiOperation(value = "Delete theme", response = ResponseEntity.class)
    public ResponseEntity deleteTheme(@PathVariable Long themeId) {
        themeService.deleteThemeById(themeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Validated(UpdateGroup.class)
    @PutMapping("/{themeId}")
    @ApiOperation(value = "Update theme", response = ResponseEntity.class)
    public ResponseEntity updateTheme(@PathVariable Long themeId, @RequestBody @Valid ThemeDto themeDto) {
        Theme themeById = themeService.getThemeById(themeId);
        if (themeById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Theme updatedTheme = themeMapper.themeDtoToThemeEntity(themeDto);
        updatedTheme.setPosition(themeById.getPosition());
        themeService.updateTheme(updatedTheme);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*
     * Перемещение темы на одну позицию вверх
     *
     * @param themeId - ID перемещаемой темы
     * */
    @PatchMapping("/{themeId}/position/up")
    @ApiOperation(value = "Move them position Up", response = ResponseEntity.class)
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
    @ApiOperation(value = "Move them position Down", response = ResponseEntity.class)
    public ResponseEntity<String> moveThemePositionDown(@PathVariable String themeId) {
        try {
            themeService.shiftThemePosition(Long.parseLong(themeId), 1);
            return ResponseEntity.ok("Тема перемещена на одну позицию вниз");
        } catch (ProcessInputException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
