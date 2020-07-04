package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.exceptions.ProcessInputException;
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

    @Transactional
    @Override
    public void shiftThemePosition(Long themeId, int shift) throws ProcessInputException {

        Theme themeToShift = themeDao.getById(themeId);
        int currentPosition = themeToShift.getPosition();
        int newPosition = currentPosition + shift;

        int minThemePosition = themeDao.getThemeMinPosition();
        int maxThemePosition = themeDao.getThemeMaxPosition();

        if (newPosition <= maxThemePosition && newPosition >= minThemePosition) {
//            Смещаем другие темы, которые находятся между старой и новой позициями изначально смещаемой темы themeToShift
            if (shift > 0) {
                themeDao.shiftThemePosition(currentPosition, newPosition, -1);
            } else if (shift < 0) {
                themeDao.shiftThemePosition(newPosition, currentPosition, 1);
                }
//            Смещаем выранную тему (themeToShift) на указанное количество позиций (shift)
            themeToShift.setPosition(newPosition);
            themeDao.update(themeToShift);
        } else {
            StringBuilder error = new StringBuilder();
            error.append("Ошибка: Тема не может быть смещена на ")
                    .append(Math.abs(shift))
                    .append(" позиций. Новое положение темы должно быть в диапазоне между ")
                    .append(minThemePosition)
                    .append(" и ")
                    .append(maxThemePosition)
                    .append(". Текущее положение темы: ")
                    .append(currentPosition);
            throw new ProcessInputException(error.toString());
        }
    }
}
