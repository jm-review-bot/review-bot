package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.model.Theme;

import java.util.List;
import javax.persistence.TypedQuery;

@Repository
public class ThemeDaoImpl extends AbstractDao<Long, Theme> implements ThemeDao {

    private static final String THEME_T_WHERE_T_POSITION_POSITION = "SELECT t FROM Theme t WHERE t.position = :position";

    public ThemeDaoImpl() {
        super(Theme.class);
    }

    @Override
    public List<Theme> getPassedThemesByUser(Integer vkId) {
        return (List<Theme>) entityManager.createNativeQuery("SELECT t.* FROM theme t " +
                "join review r on t.id = r.theme_id join student_review sr on r.id = sr.review_id join users u on sr.student_id = u.id " +
                "where u.vk_id = :vk_id AND r.is_open = false AND sr.is_passed = true", Theme.class)
                .setParameter("vk_id", vkId)
                .getResultList();
    }

    @Override
    public Theme getByPosition(Integer position) {
        TypedQuery<Theme> query = entityManager.createQuery(THEME_T_WHERE_T_POSITION_POSITION, Theme.class);
        query.setParameter("position", position);
        return query.getSingleResult();
    }

}
