package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.QuestionDao;
import spring.app.dto.QuestionDto;
import spring.app.model.Question;
import spring.app.service.abstraction.QuestionService;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private QuestionDao questionDao;

    @Autowired
    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Transactional
    @Override
    public void addQuestion(Question question) {
        questionDao.save(question);
    }

    @Override
    public Question getQuestionById(Long id) {
        return questionDao.getById(id);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionDao.getAll();
    }

    @Transactional
    @Override
    public void updateQuestion(Question question) {
        questionDao.update(question);
    }

    /*
     * Метод производит удаление вопроса из темы,
     * а затем смещает позиции нижестоящих вопросов на одну вверх
     * */
    @Transactional
    @Override
    public void deleteQuestionById(Long id) {
        Question question = questionDao.getById(id);
        if (question != null) {
            Long themeIdOfQuestion = question.getTheme().getId();
            int questionPosition = question.getPosition();
            int maxPosition = questionDao.getQuestionMaxPositionByThemeId(themeIdOfQuestion);

            questionDao.deleteById(id);
            questionDao.shiftQuestionsPosition(themeIdOfQuestion, questionPosition + 1, maxPosition, -1);
        }
    }

    @Override
    public List<Question> getQuestionsByReviewId(Long reviewId) {
        return questionDao.getQuestionsByReviewId(reviewId);
    }

    @Override
    public Question getQuestionByStudentReviewAnswerId(Long studentReviewAnswerId) {
        return questionDao.getQuestionByStudentReviewAnswerId(studentReviewAnswerId);
    }

    @Override
    public List<Question> getQuestionsByThemeId(Long themeId) {
        return questionDao.getQuestionsByThemeId(themeId);
    }

    @Override
    public List<QuestionDto> getAllQuestionDtoByTheme(Long themeId) {
        return questionDao.getAllQuestionDtoByTheme(themeId);
    }

    @Override
    public QuestionDto getQuestionDtoById(Long id) {
        return questionDao.getQuestionDtoById(id);
    }

    /*
    * Возвращает успешность проведения изменения позиции в сущности Question
    * */
    @Transactional
    @Override
    public boolean changeQuestionPositionByThemeIdAndQuestionIdAndPositionShift(Long themeId, Long questionId, Integer positionChange) {
        Question currentQuestion = questionDao.getQuestionByThemeIdAndId(themeId, questionId);
        if (currentQuestion == null) {
            return false;
        }

        Integer currentPosition = currentQuestion.getPosition();

        Integer nextPositionValue;
        int positionLow;
        int positionHigh;
        int positionShift;
        if (positionChange > 0) {
            Integer maxPositionValue = questionDao.getQuestionMaxPositionByThemeId(themeId);
            if (currentPosition == maxPositionValue) {
                return false;
            }

            nextPositionValue = Math.min(maxPositionValue, currentPosition + positionChange);

            positionLow = currentPosition + 1;
            positionHigh = currentPosition + positionChange;
            positionShift = -1;
        } else {
            Integer minPositionValue = questionDao.getQuestionMinPositionByThemeId(themeId);
            if (currentPosition == minPositionValue) {
                return false;
            }

            nextPositionValue = Math.max(minPositionValue, currentQuestion.getPosition() + positionChange);

            positionLow = currentPosition - Math.abs(positionChange);
            positionHigh = currentPosition - 1;
            positionShift = 1;
        }

        questionDao.shiftQuestionsPosition(themeId, positionLow, positionHigh, positionShift);

        currentQuestion.setPosition(nextPositionValue);
        questionDao.update(currentQuestion);
        return true;
    }


}

