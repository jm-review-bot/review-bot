package spring.app.dao.abstraction;

import spring.app.model.Theme;

import java.util.List;

public interface ThemeDao extends GenericDao<Long, Theme> {

    Theme getByPosition(Integer position);

    List<Theme> getPassedThemesByUser(Integer vkId);

    Theme getThemeByReviewId(Long reviewId);
}
