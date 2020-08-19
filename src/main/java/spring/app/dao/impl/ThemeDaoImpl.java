package spring.app.dao.impl;

import org.springframework.stereotype.Repository;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.dto.FixedThemeDto;
import spring.app.dto.ThemeDto;
import spring.app.model.FreeTheme;
import spring.app.model.Theme;
import spring.app.model.User;

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
        return entityManager.createNativeQuery("SELECT t.* FROM theme t " +
                "join review r on t.id = r.theme_id join student_review sr on r.id = sr.review_id join users u on sr.student_id = u.id " +
                "where u.vk_id = :vk_id AND r.is_open = false AND sr.is_passed = true", Theme.class)
                .setParameter("vk_id", vkId)
                .getResultList();
    }

    @Override
    public List<Theme> getNonPassedThemesByUser(Integer vkId) {
        return entityManager.createQuery("SELECT t FROM Theme t JOIN Review r ON t.id = r.theme.id JOIN StudentReview sr on r.id = sr.review.id JOIN User u ON sr.user.id = u.id WHERE u.vkId = :vk_id AND r.isOpen = false AND (sr.isPassed = false OR sr.isPassed IS NULL)", Theme.class)
                .setParameter("vk_id", vkId)
                .getResultList();
    }

    @Override
    public List<Theme> getAllThemesUpToPosition(Integer position) {
        return entityManager.createQuery("SELECT t FROM Theme t WHERE t.position <= :position ORDER BY t.position", Theme.class)
                .setParameter("position", position)
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
        Integer maxPosition = entityManager.createQuery("SELECT max(t.position) FROM Theme t", Integer.class)
                .getSingleResult();
        return maxPosition == null ? 0 : maxPosition;
    }

    @Override
    public Integer getThemeMinPositionValue() {
        Integer minPosition = entityManager.createQuery("SELECT min(t.position) FROM Theme t", Integer.class)
                .getSingleResult();
        return minPosition == null ? 0 : minPosition;
    }

    @Override
    public List<ThemeDto> getAllThemesDto() {
        return entityManager.createQuery("SELECT new spring.app.dto.ThemeDto(t.id, t.title, t.criticalWeight, t.position, t.reviewPoint, t.themeType) FROM Theme t ORDER BY t.position", ThemeDto.class)
                .getResultList();
    }

    @Override
    public FixedThemeDto getFixedThemeDtoById(Long themeId) {
        List<FixedThemeDto> fixedThemeDtoByIdList = entityManager.createQuery("SELECT new spring.app.dto.FixedThemeDto(t.id, t.title, t.criticalWeight, t.position, t.reviewPoint) FROM FixedTheme t WHERE t.id =:theme_id", FixedThemeDto.class)
                .setParameter("theme_id", themeId)
                .getResultList();
        return fixedThemeDtoByIdList.size() > 0 ? fixedThemeDtoByIdList.get(0) : null;
    }

    @Override
    public void shiftThemePosition(Integer positionLow, Integer positionHigh, Integer positionShift) {
        entityManager.createQuery("UPDATE Theme t SET t.position = t.position + (:position_shift) WHERE t.position BETWEEN :position_low AND :position_high")
                .setParameter("position_shift", positionShift)
                .setParameter("position_low", positionLow)
                .setParameter("position_high", positionHigh)
                .executeUpdate();
    }

    @Override
    public List<Theme> getFreeThemesByExaminerId(Long examinerId) {
        return entityManager.createQuery("SELECT t FROM FreeTheme t JOIN t.examiners e WHERE e.id = :examiner_id ORDER BY t.position", Theme.class)
                .setParameter("examiner_id", examinerId)
                .getResultList();
    }

    @Override
    public List<Theme> getAllThemesByThemeType(String themeType) {
        if(themeType.equals("fixed")) {
            return entityManager.createQuery("SELECT t FROM FixedTheme t", Theme.class)
                    .getResultList();
        } else if(themeType.equals("free")) {
            return entityManager.createQuery("SELECT t FROM FreeTheme t", Theme.class)
                    .getResultList();
        } else return null;
    }

    @Override
    public FreeTheme getFreeThemeById(long id) {
        List<FreeTheme> freeThemes = entityManager.createQuery("select ft from FreeTheme ft where ft.id =:id")
                .setParameter("id" , id)
                .getResultList();
        return freeThemes.size() > 0 ? freeThemes.get(0) : null;
    }

    @Override
    public List<User> getExaminersByFreeThemeId(Long freeThemeId) {
        return entityManager.createQuery("SELECT ft.examiners FROM FreeTheme ft WHERE ft.id = :free_theme_id")
                .setParameter("free_theme_id", freeThemeId)
                .getResultList();
    }

    @Override
    public boolean isFreeTheme(Long themeId) {
        List<Theme> themeList = entityManager.createQuery("SELECT t FROM FreeTheme t WHERE t.id = :theme_id", Theme.class)
                .setParameter("theme_id", themeId)
                .getResultList();
        return themeList.size() > 0;
    }

    @Override
    public List<ThemeDto> themesSearch(String searchString) {
        return entityManager.createQuery("SELECT DISTINCT new spring.app.dto.ThemeDto(t.id, t.title, t.criticalWeight, t.position, t.reviewPoint, t.themeType) FROM Theme t LEFT JOIN Question q ON q.fixedTheme.id = t.id WHERE LOWER(t.title) LIKE LOWER(:search) OR LOWER(q.question) LIKE LOWER(:search) ORDER BY t.position", ThemeDto.class)
                .setParameter("search", "%" + searchString + "%")
                .getResultList();
    }
}
