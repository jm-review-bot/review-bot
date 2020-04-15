package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.model.Theme;
import spring.app.service.abstraction.ThemeService;

import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {

    private ThemeDao themeDao;

    @Autowired
    public ThemeServiceImpl(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @Transactional
    @Override
    public void addTheme(Theme theme) {
        themeDao.save(theme);
    }

    @Override
    public Theme getThemeById(Long id) {
        return themeDao.getById(id);
    }

    @Override
    public List<Theme> getAllThemes() {
        return themeDao.getAll();
    }

    @Transactional
    @Override
    public void updateTheme(Theme theme) {
        themeDao.update(theme);
    }

    @Transactional
    @Override
    public void deleteThemeById(Long id) {
        themeDao.deleteById(id);
    }

    @Override
    public Theme getByPosition(Integer position){
        return themeDao.getByPosition(position);
    }

    @Override
    public List<Theme> getPassedThemesByUser(Integer vkId) {
        return themeDao.getPassedThemesByUser(vkId);
    }

    @Override
    public Theme getThemeByReviewId(Long reviewId) {
        return themeDao.getThemeByReviewId(reviewId);
    }
}
