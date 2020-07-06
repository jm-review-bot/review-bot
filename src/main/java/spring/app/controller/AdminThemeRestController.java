package spring.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.ThemeDto;
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
        ThemeDto themeDtoById = themeService.getThemeDtoById(themeId);
        if (themeDtoById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(themeDtoById);
    }

    @Validated(CreateGroup.class)
    @PostMapping
    public ResponseEntity<ThemeDto> createTheme(@RequestBody @Valid ThemeDto themeDto) {
        Theme theme = themeMapper.themeDtoToThemeEntity(themeDto);
        theme.setPosition(themeService.getThemeMaxPositionValue() + 1); // автоматическое выстановление позиции
        themeService.addTheme(theme);
        return ResponseEntity.status(HttpStatus.CREATED).body(themeMapper.themeEntityToThemeDto(theme));
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity deleteTheme(@PathVariable Long themeId) {
        themeService.deleteThemeById(themeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Validated(UpdateGroup.class)
    @PutMapping("/{themeId}")
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
}
