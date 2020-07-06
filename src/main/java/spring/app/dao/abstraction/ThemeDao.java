package spring.app.dao.abstraction;

import spring.app.dto.ThemeDto;
import spring.app.model.Theme;

import java.util.List;

public interface ThemeDao extends GenericDao<Long, Theme> {

    Theme getByPosition(Integer position);

    List<Theme> getPassedThemesByUser(Integer vkId);

    Theme getThemeByReviewId(Long reviewId);

    Integer getThemeMaxPositionValue();

    List<ThemeDto> getAllThemesDto();

    ThemeDto getThemeDtoById(Long themeId);
}
