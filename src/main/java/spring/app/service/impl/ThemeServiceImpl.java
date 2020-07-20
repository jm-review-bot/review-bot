package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.QuestionDao;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.dao.abstraction.ThemeDao;
import spring.app.dto.ThemeDto;
import spring.app.exceptions.ProcessInputException;
import spring.app.model.Question;
import spring.app.model.Review;
import spring.app.model.Theme;
import spring.app.service.abstraction.ReviewService;
import spring.app.service.abstraction.ThemeService;

import java.util.Arrays;
import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {

    private ThemeDao themeDao;
    private QuestionDao questionDao;
    private ReviewDao reviewDao;

    @Autowired
    public ThemeServiceImpl(ThemeDao themeDao,
                            QuestionDao questionDao,
                            ReviewDao reviewDao) {
        this.themeDao = themeDao;
        this.questionDao  = questionDao;
        this.reviewDao = reviewDao;
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

    /*
    * Метод перед удалением темы проверяет, есть ли связанные с ней вопросы.
    * Если вопросы есть, то в первую очередь удаляются найденные вопросы,
    * а потом только сама тема
    *
    * @param id - ID удаляемой темы
    * */
    @Transactional
    @Override
    public void deleteThemeById(Long id) {
        List<Question> questions = questionDao.getQuestionsByThemeId(id);
        questionDao.removeAll(questions);
        List<Review> reviews = reviewDao.getAllReviewsByThemeId(id);
        reviewDao.removeAll(reviews);
        themeDao.deleteById(id);
    }

    @Override
    public Theme getByPosition(Integer position) {
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

    @Override
    public Integer getThemeMaxPositionValue() {
        return themeDao.getThemeMaxPositionValue();
    }

    @Override
    public Integer getThemeMinPositionValue() {
        return themeDao.getThemeMinPositionValue();
    }

    @Override
    public List<ThemeDto> getAllThemesDto() {
        return themeDao.getAllThemesDto();
    }

    @Override
    public ThemeDto getThemeDtoById(Long themeId) {
        return themeDao.getThemeDtoById(themeId);
    }

    @Transactional
    @Override
    public void shiftThemePosition(Long themeId, int shift) throws ProcessInputException {

        Theme themeToShift = themeDao.getById(themeId);
        int currentPosition = themeToShift.getPosition();
        int newPosition = currentPosition + shift;

        int minThemePosition = themeDao.getThemeMinPositionValue();
        int maxThemePosition = themeDao.getThemeMaxPositionValue();

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
            String error = String.format(
                    "Ошибка: Тема не может быть смещена на %d позиций. Новое положение темы должно быть в диапазоне между %d и %d. Текущее положение темы: %d.",
                    Math.abs(shift),
                    minThemePosition,
                    maxThemePosition,
                    currentPosition
            );
            throw new ProcessInputException(error.toString());
        }
    }
}
