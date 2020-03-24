package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.StudentReviewAnswerDao;
import spring.app.model.StudentReviewAnswer;
import spring.app.service.abstraction.StudentReviewAnswerService;

import java.util.List;

@Service
public class StudentReviewAnswerServiceImpl implements StudentReviewAnswerService {

    private StudentReviewAnswerDao studentReviewAnswerDao;

    @Autowired
    public StudentReviewAnswerServiceImpl(StudentReviewAnswerDao studentReviewAnswerDao) {
        this.studentReviewAnswerDao = studentReviewAnswerDao;
    }

    @Transactional
    @Override
    public void addStudentReviewAnswer(StudentReviewAnswer studentReviewAnswer) {
        studentReviewAnswerDao.save(studentReviewAnswer);
    }

    @Override
    public StudentReviewAnswer getStudentReviewAnswerById(Long id) {
        return studentReviewAnswerDao.getById(id);
    }

    @Override
    public List<StudentReviewAnswer> getAllStudentReviewAnswers() {
        return studentReviewAnswerDao.getAll();
    }

    @Transactional
    @Override
    public void updateStudentReviewAnswer(StudentReviewAnswer studentReviewAnswer) {
        studentReviewAnswerDao.update(studentReviewAnswer);
    }

    @Transactional
    @Override
    public void deleteStudentReviewAnswerById(Long id) {
        studentReviewAnswerDao.deleteById(id);
    }
}
