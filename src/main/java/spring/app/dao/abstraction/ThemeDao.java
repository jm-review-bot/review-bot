package spring.app.dao.abstraction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dto.FixedThemeDto;
import spring.app.dto.ThemeDto;
import spring.app.model.FreeTheme;
import spring.app.model.Theme;
import spring.app.model.User;

import java.util.List;

public interface ThemeDao extends GenericDao<Long, Theme> {

    List<Theme> getAllThemesByThemeType(String themeType);

    Theme getByPosition(Integer position);

    List<Theme> getPassedThemesByUser(Integer vkId);

    List<Theme> getNonPassedThemesByUser(Integer vkId);

    List<Theme> getAllThemesUpToPosition(Integer position);

    Theme getThemeByReviewId(Long reviewId);

    Integer getThemeMaxPositionValue();

    Integer getThemeMinPositionValue();

    List<ThemeDto> getAllThemesDto();

    FixedThemeDto getFixedThemeDtoById(Long themeId);

    @Transactional(propagation = Propagation.MANDATORY)
    void shiftThemePosition(Integer positionLow, Integer positionHigh, Integer positionShift);

    List<Theme> getFreeThemesByExaminerId(Long examinerId);

    FreeTheme getFreeThemeById (long id);

    List<User> getExaminersByFreeThemeId(Long freeThemeId);

    boolean isFreeTheme(Long themeId);

    List<ThemeDto> themesSearch(String searchString);
}
