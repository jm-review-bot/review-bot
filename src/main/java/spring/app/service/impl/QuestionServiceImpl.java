package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.QuestionDao;
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

    @Transactional
    @Override
    public void deleteQuestionById(Long id) {
        questionDao.deleteById(id);
    }
}

