package spring.app.dao.abstraction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dto.FixedThemeDto;
import spring.app.dto.ThemeDto;
import spring.app.model.FreeTheme;
import spring.app.dto.FreeThemeDto;
import spring.app.model.Theme;

import java.util.List;

public interface ThemeDao extends GenericDao<Long, Theme> {

    Theme getByPosition(Integer position);

    List<Theme> getPassedThemesByUser(Integer vkId);

    Theme getThemeByReviewId(Long reviewId);

    Integer getThemeMaxPositionValue();

    Integer getThemeMinPositionValue();

    List<FixedThemeDto> getAllFixedThemesDto();

    List<FreeThemeDto> getAllFreeThemesDto();

    List<ThemeDto> getAllThemesDto();

    FixedThemeDto getFixedThemeDtoById(Long themeId);

    @Transactional(propagation = Propagation.MANDATORY)
    void shiftThemePosition(Integer positionLow, Integer positionHigh, Integer positionShift);

    List<Theme> getFreeThemesByExaminerId(Long examinerId);

    FreeTheme getFreeThemeById (long id);
}
