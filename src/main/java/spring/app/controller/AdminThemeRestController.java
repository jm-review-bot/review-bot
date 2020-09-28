package spring.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.ThemeDto;
import spring.app.exceptions.ProcessInputException;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import spring.app.mapper.ThemeMapper;
import spring.app.model.Theme;
import spring.app.model.User;
import spring.app.service.abstraction.ThemeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/admin/theme")
@Api(value = "Theme controller")
public class AdminThemeRestController {

    private final static Logger logger = LoggerFactory.getLogger(AdminThemeRestController.class);

    private ThemeService themeService;
    private ThemeMapper themeMapper;

    public AdminThemeRestController(ThemeService themeService, ThemeMapper themeMapper) {
        this.themeService = themeService;
        this.themeMapper = themeMapper;
    }

    @GetMapping
    @ApiOperation(value = "View a list of all themes")
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemesDto());
    }

    @GetMapping("/{themeId}")
    @ApiOperation(value = "Get the theme")
    public ResponseEntity<Optional<ThemeDto>> getThemeById(@ApiParam(value = "Theme Id", required = true) @PathVariable Long themeId) {
        Optional<ThemeDto> optionalThemeDto = themeService.getThemeDtoById(themeId);
        if (!optionalThemeDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(optionalThemeDto);
    }

    @Validated(CreateGroup.class)
    @PostMapping
    @ApiOperation(value = "Create a new theme")
    public ResponseEntity<ThemeDto> createTheme(@ApiParam(value = "Theme model in DTO", required = true) @RequestBody @Valid ThemeDto themeDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        logger.info("Админ (vkId={}) добавил тему (ID={} , Title={})" ,
                user.getVkId() , addedThemeDto.getId() , themeDto.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedThemeDto);
    }

    @DeleteMapping("/{themeId}")
    @ApiOperation(value = "Delete the theme")
    public ResponseEntity deleteTheme(@ApiParam(value = "Theme id", required = true) @PathVariable Long themeId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Theme theme = themeService.getThemeById(themeId);
        themeService.deleteThemeById(themeId);
        logger.info(
                "Админ (vkId={}) удалил тему (ID={} , Title={})" ,
                user.getVkId() , themeId ,  theme.getTitle() );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Validated(UpdateGroup.class)
    @PutMapping("/{themeId}")
    @ApiOperation(value = "Update the theme")
    public ResponseEntity updateTheme(@ApiParam(value = "Theme id", required = true) @PathVariable Long themeId,
                                      @ApiParam(value = "Theme model in DTO", required = true) @RequestBody ThemeDto themeDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Theme themeById = themeService.getThemeById(themeId);
        Theme updatedTheme=null;
        if (themeById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if(themeDto.getType().equals("fixed")){
            updatedTheme = themeMapper.fixedThemeDtoToFixedThemeEntity(themeDto);
        } else if(themeDto.getType().equals("free")){
            updatedTheme = themeMapper.freeThemeDtoToFreeThemeEntity(themeDto);
        }

        updatedTheme.setPosition(themeById.getPosition());
        themeService.updateTheme(updatedTheme);
        logger.info(
                "Админ (vkId={}) изменил тему (ID={} , Title={})" ,
                user.getVkId(), themeId, updatedTheme.getTitle()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*
     * Перемещение темы на одну позицию вверх
     *
     * @param themeId - ID перемещаемой темы
     * */
    @PatchMapping("/{themeId}/position/up")
    @ApiOperation(value = "Move the theme top")
    public ResponseEntity<String> moveThemePositionUp (@ApiParam(value = "Theme id", required = true) @PathVariable String themeId) {
        try {
            Theme theme = themeService.getThemeById(Long.parseLong(themeId));
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            themeService.shiftThemePosition(Long.parseLong(themeId), -1);
            logger.info(
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
    @ApiOperation(value = "Move the theme down")
    public ResponseEntity<String> moveThemePositionDown(@ApiParam(value = "Theme id", required = true) @PathVariable String themeId) {
        try {
            Theme theme = themeService.getThemeById(Long.parseLong(themeId));
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            themeService.shiftThemePosition(Long.parseLong(themeId), 1);
            logger.info(
                    "Админ (vkId={}) переместил тему (ID={} , Title={}) на одну позицию вниз" ,
                    user.getVkId() , themeId, theme.getTitle()
            );
            return ResponseEntity.ok("Тема перемещена на одну позицию вниз");
        } catch (ProcessInputException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
