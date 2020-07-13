package spring.app.dao.abstraction;

import spring.app.dto.FixedThemeDto;
import spring.app.model.Theme;

import java.util.List;

public interface ThemeDao extends GenericDao<Long, Theme> {

    Theme getByPosition(Integer position);

    List<Theme> getPassedThemesByUser(Integer vkId);

    Theme getThemeByReviewId(Long reviewId);

    Integer getThemeMaxPositionValue();

    Integer getThemeMinPositionValue();

    List<FixedThemeDto> getAllThemesDto();

    FixedThemeDto getThemeDtoById(Long themeId);

    void shiftThemePosition(Integer positionLow, Integer positionHigh, Integer positionShift);
}
