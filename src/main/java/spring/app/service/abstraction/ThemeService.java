package spring.app.service.abstraction;

import spring.app.dto.ThemeDto;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.FreeTheme;
import spring.app.model.Theme;
import spring.app.model.User;

import java.util.List;

public interface ThemeService {

    List<Theme> getAllThemesByThemeType(String themeType);

    void addTheme(Theme theme);

    Theme getThemeById(Long id);

    List<Theme> getAllThemes();

    void updateTheme(Theme theme);

    void deleteThemeById(Long id);

    Theme getByPosition(Integer position);

    List<Theme> getPassedThemesByUser(Integer vkId);

    List<Theme> getNonPassedThemesByUser(Integer vkId);

    List<Theme> getAllThemesUpToPosition(Integer position);

    Theme getThemeByReviewId(Long reviewId);

    Integer getThemeMaxPositionValue();

    Integer getThemeMinPositionValue();

    List<ThemeDto> getAllThemesDto();

    void shiftThemePosition(Long themeId, int shift) throws ProcessInputException;

    List<Theme> getFreeThemesByExaminerId(Long examinerId);

    FreeTheme getFreeThemeById (long id);

    List<User> getExaminersByFreeThemeId(Long freeThemeId);

    boolean isFreeTheme(Long themeId);

    List<ThemeDto> themesSearch(String searchString);

    ThemeDto getThemeDtoById(Long themeId);
}
