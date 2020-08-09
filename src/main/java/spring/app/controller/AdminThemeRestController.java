package spring.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.FixedThemeDto;
import spring.app.dto.ThemeDto;
import spring.app.exceptions.ProcessInputException;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.mapper.ThemeMapper;
import spring.app.model.FixedTheme;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.ThemeService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/theme")
public class AdminThemeRestController {

    private final static Logger log = LoggerFactory.getLogger(AdminThemeRestController.class);

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
    public ResponseEntity<FixedThemeDto> getThemeById(@PathVariable Long themeId) {
        FixedThemeDto fixedThemeDtoById = themeService.getFixedThemeDtoById(themeId);
        if (fixedThemeDtoById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(fixedThemeDtoById);
    }

    @Validated(CreateGroup.class)
    @PostMapping
    public ResponseEntity<ThemeDto> createTheme(@RequestBody @Valid ThemeDto themeDto) {
        Theme theme;
        ThemeDto addedThemeDto = null;
        if (themeDto.getType().equals("fixed")) {
            theme = themeMapper.fixedThemeDtoToFixedThemeEntity(themeDto);
            themeService.addTheme(theme);
            addedThemeDto = themeMapper.fixedThemeEntityToFixedThemeDto(theme);
        } else if (themeDto.getType().equals("free")) {
            theme = themeMapper.freeThemeDtoToFreeThemeEntity(themeDto);
            themeService.addTheme(theme);
            addedThemeDto = themeMapper.freeThemeEntityToFreeThemeDto(theme);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(addedThemeDto);
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity deleteTheme(@PathVariable Long themeId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Theme theme = themeService.getThemeById(themeId);
        themeService.deleteThemeById(themeId);
        log.info(
                "Админ (vkId={}) удалил тему (ID={} , Title={})" ,
                user.getVkId() , themeId ,  theme.getTitle() );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Validated(UpdateGroup.class)
    @PutMapping("/{themeId}")
    public ResponseEntity updateTheme(@PathVariable Long themeId, @RequestBody @Valid FixedThemeDto fixedThemeDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Theme themeById = themeService.getThemeById(themeId);
        if (themeById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        FixedTheme updatedFixedTheme = themeMapper.fixedThemeDtoToFixedThemeEntity(fixedThemeDto);
        updatedFixedTheme.setPosition(themeById.getPosition());
        themeService.updateTheme(updatedFixedTheme);
        log.info(
                "Админ (vkId={}) изменил тему (ID={} , Title={})" ,
                user.getVkId(), themeId, updatedFixedTheme.getTitle()
        );
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
            Theme theme = themeService.getThemeById(Long.parseLong(themeId));
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            themeService.shiftThemePosition(Long.parseLong(themeId), -1);
            log.info(
                    "Админ (vkId={}) переместил тему (ID={} , Title={}) на одну позицию вверх" ,
                    user.getVkId(), themeId, theme.getTitle()
            );
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
            Theme theme = themeService.getThemeById(Long.parseLong(themeId));
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            themeService.shiftThemePosition(Long.parseLong(themeId), 1);
            log.info(
                    "Админ (vkId={}) переместил тему (ID={} , Title={}) на одну позицию вниз" ,
                    user.getVkId() , themeId, theme.getTitle()
            );
            return ResponseEntity.ok("Тема перемещена на одну позицию вниз");
        } catch (ProcessInputException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
