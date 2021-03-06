package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.dto.ThemeDto;
import spring.app.model.Theme;

import java.util.List;

@Repository
public class ThemeDaoImpl extends AbstractDao<Long, Theme> implements ThemeDao {

    public ThemeDaoImpl() {
        super(Theme.class);
    }

    @Override
    public List<Theme> getAll() {
        return entityManager.createQuery("SELECT t FROM Theme t ORDER BY t.position", Theme.class).getResultList();
    }

    /**
     * Метод возвращает тему по ее порядковому номеру
     *
     * @param position
     */
    public Theme getByPosition(Integer position) {
        return entityManager.createQuery("SELECT t FROM Theme t WHERE t.position = :position", Theme.class)
                .setParameter("position", position)
                .getSingleResult();
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
    public Theme getThemeByReviewId(Long reviewId) {
        return entityManager.createQuery("SELECT t FROM Review r JOIN r.theme t WHERE r.id =:id", Theme.class)
                .setParameter("id", reviewId)
                .getSingleResult();
    }

    @Override
    public Integer getThemeMaxPositionValue() {
        List<Integer> maxPosition = entityManager.createQuery("SELECT max(t.position) FROM Theme t", Integer.class)
                .getResultList();
        return maxPosition.size() > 0 ? maxPosition.get(0) : 0;
    }

    @Override
    public Integer getThemeMinPositionValue() {
        List<Integer> maxPosition = entityManager.createQuery("SELECT min(t.position) FROM Theme t", Integer.class)
                .getResultList();
        return maxPosition.size() > 0 ? maxPosition.get(0) : 0;
    }

    @Override
    public List<ThemeDto> getAllThemesDto() {
        return entityManager.createQuery("SELECT new spring.app.dto.ThemeDto(t.id, t.title, t.criticalWeight, t.position, t.reviewPoint) FROM Theme t ORDER BY t.position", ThemeDto.class)
                .getResultList();
    }

    @Override
    public ThemeDto getThemeDtoById(Long themeId) {
        List<ThemeDto> themeDtoByIdList = entityManager.createQuery("SELECT new spring.app.dto.ThemeDto(t.id, t.title, t.criticalWeight, t.position, t.reviewPoint) FROM Theme t WHERE t.id =:theme_id", ThemeDto.class)
                .setParameter("theme_id", themeId)
                .getResultList();
        return themeDtoByIdList.size() > 0 ? themeDtoByIdList.get(0) : null;
    }

    @Override
    public void shiftThemePosition(Integer positionLow, Integer positionHigh, Integer positionShift) {
        entityManager.createQuery("UPDATE Theme t SET t.position = t.position + (:position_shift) WHERE t.position BETWEEN :position_low AND :position_high")
                .setParameter("position_shift", positionShift)
                .setParameter("position_low", positionLow)
                .setParameter("position_high", positionHigh)
                .executeUpdate();
    }
}
