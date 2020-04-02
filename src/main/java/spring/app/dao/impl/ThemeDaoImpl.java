package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.model.Theme;

import javax.persistence.TypedQuery;

@Repository
public class ThemeDaoImpl extends AbstractDao<Long, Theme> implements ThemeDao {

    private static final String THEME_T_WHERE_T_POSITION_POSITION = "SELECT t FROM Theme t WHERE t.position = :position";

    public ThemeDaoImpl() {
        super(Theme.class);
    }

    public Theme getByPosition(Integer position) {
        TypedQuery<Theme> query = entityManager.createQuery(THEME_T_WHERE_T_POSITION_POSITION, Theme.class);
        query.setParameter("position", position);
        return query.getSingleResult();
    }

}
