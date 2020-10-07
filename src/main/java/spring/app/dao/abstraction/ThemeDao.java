package spring.app.dao.abstraction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dto.ThemeDto;
import spring.app.model.FreeTheme;
import spring.app.model.Theme;
import spring.app.model.User;

import java.util.List;
import java.util.Optional;

public interface ThemeDao extends GenericDao<Long, Theme> {

    List<Theme> getAllThemesByThemeType(String themeType);

    Optional<Theme> getByPosition(Integer position);

    List<Theme> getPassedThemesByUser(Integer vkId);

    List<Theme> getNonPassedThemesByUser(Integer vkId);

    List<Theme> getAllThemesUpToPosition(Integer position);

    Optional<Theme> getThemeByReviewId(Long reviewId);

    Integer getThemeMaxPositionValue();

    Integer getThemeMinPositionValue();

    List<ThemeDto> getAllThemesDto();

    @Transactional(propagation = Propagation.MANDATORY)
    void shiftThemePosition(Integer positionLow, Integer positionHigh, Integer positionShift);

    List<Theme> getFreeThemesByExaminerId(Long examinerId);

    Optional<FreeTheme> getFreeThemeById (long id);

    List<User> getExaminersByFreeThemeId(Long freeThemeId);

    boolean isFreeTheme(Long themeId);

    List<ThemeDto> themesSearch(String searchString);

    Optional<ThemeDto> getThemeDtoById(Long themeId);
}
