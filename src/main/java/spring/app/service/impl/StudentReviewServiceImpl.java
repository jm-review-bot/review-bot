package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.StudentReviewDao;
import spring.app.model.StudentReview;
import spring.app.service.abstraction.StudentReviewService;

import java.util.List;

@Service
public class StudentReviewServiceImpl implements StudentReviewService {

    private StudentReviewDao studentReviewDao;

    @Autowired
    public StudentReviewServiceImpl(StudentReviewDao studentReviewDao) {
        this.studentReviewDao = studentReviewDao;
    }

    @Transactional
    @Override
    public void addStudentReview(StudentReview studentReview) {
        studentReviewDao.save(studentReview);
    }

    @Override
    public StudentReview getStudentReviewById(Long id) {
        return studentReviewDao.getById(id);
    }

    @Override
    public List<StudentReview> getAllStudentReview() {
        return studentReviewDao.getAll();
    }

    @Transactional
    @Override
    public void updateStudentReview(StudentReview studentReview) {
        studentReviewDao.update(studentReview);
    }

    @Transactional
    @Override
    public void deleteStudentReviewById(Long id) {
        studentReviewDao.deleteById(id);
    }

    @Override
    public StudentReview getStudentReviewIfAvailableAndOpen(Long idUser){
        return studentReviewDao.getStudentReviewIfAvailableAndOpen(idUser);
    }

    @Override
    public Long getNumberStudentReviewByIdReview(Long idReview) {
        return studentReviewDao.getNumberStudentReviewByIdReview(idReview);
    }
}
