package spring.app.service.abstraction;

import spring.app.dto.FixedThemeDto;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Theme;

import java.util.List;

public interface ThemeService {

    void addTheme(Theme theme);

    Theme getThemeById(Long id);

    List<Theme> getAllThemes();

    void updateTheme(Theme theme);

    void deleteThemeById(Long id);

    Theme getByPosition(Integer position);

    List<Theme> getPassedThemesByUser(Integer vkId);

    Theme getThemeByReviewId(Long reviewId);

    Integer getThemeMaxPositionValue();

    Integer getThemeMinPositionValue();

    List<FixedThemeDto> getAllFixedThemesDto();

    FixedThemeDto getFixedThemeDtoById(Long themeId);

    void shiftThemePosition(Long themeId, int shift) throws ProcessInputException;

    List<Theme> getFreeThemesByExaminerId(Long examinerId);
}
