package spring.app.dao.abstraction;

import spring.app.model.Theme;

public interface ThemeDao extends GenericDao<Long, Theme> {

    // убрать если не буду ползоваться
    Theme getThemeByPosition(Integer position);
}
