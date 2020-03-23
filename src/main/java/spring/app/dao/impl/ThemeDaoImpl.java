package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.model.Theme;

@Repository
public class ThemeDaoImpl extends AbstractDao<Long, Theme> implements ThemeDao {

    public ThemeDaoImpl() {
        super(Theme.class);
    }

}
