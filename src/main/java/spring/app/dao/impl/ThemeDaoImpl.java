package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.model.Theme;
import spring.app.model.User;

import javax.persistence.TypedQuery;

@Repository
public class ThemeDaoImpl extends AbstractDao<Long, Theme> implements ThemeDao {

    public ThemeDaoImpl() {
        super(Theme.class);
    }

    // убрать если не буду пользоваться
    @Override
    public Theme getThemeByPosition(Integer position) {
        TypedQuery<Theme> query = entityManager.createQuery(
                "SELECT t FROM Theme t WHERE t.position = :position", Theme.class);
        query.setParameter("position", position);
        return query.getSingleResult();
    }
}
