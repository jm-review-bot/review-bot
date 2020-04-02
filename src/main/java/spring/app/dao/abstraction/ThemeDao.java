package spring.app.dao.abstraction;

import spring.app.model.Theme;

public interface ThemeDao extends GenericDao<Long, Theme> {

    Theme getByPosition(Integer position);
}
