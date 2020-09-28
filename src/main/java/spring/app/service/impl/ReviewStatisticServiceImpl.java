package spring.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.controller.AdminThemeRestController;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.dao.abstraction.ReviewStatisticDao;
import spring.app.dao.abstraction.UserDao;
import spring.app.model.ReviewStatistic;
import spring.app.model.User;
import spring.app.service.abstraction.ReviewStatisticService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ReviewStatisticServiceImpl implements ReviewStatisticService {

    private final static Logger logger = LoggerFactory.getLogger(AdminThemeRestController.class);

    private final ReviewStatisticDao reviewStatisticDao;
    private final ReviewDao reviewDao;
    private final UserDao userDao;

    @Value("${review.max_open_reviews}")
    private int maxOpenReviews;
    @Value("${review.max_reviews_per_day}")
    private int maxReviewsPerDay;
    @Value("${review.max_reviews_without_students_in_row}")
    private int maxReviewsWithoutStudentsInRow;

    public ReviewStatisticServiceImpl(ReviewStatisticDao reviewStatisticDao,
                                      ReviewDao reviewDao,
                                      UserDao userDao) {
        this.reviewStatisticDao = reviewStatisticDao;
        this.reviewDao = reviewDao;
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public void updateReviewStatistic(ReviewStatistic reviewStatistic) {
        if (reviewStatistic != null && !reviewStatistic.isReviewBlocked()) { // Если доступ к созданию ревью уже заблокирован, то обновлять его нет необходимости
            // Актуализируются данные
            Integer userVkId = reviewStatistic.getUser().getVkId();
            Long countOpenReviews = reviewDao.getCountOpenReviewsByReviewerVkId(userVkId);
            Long countCreatedReviewForLastDay = reviewDao.getCountCompletedReviewsByReviewerVkIdFromDate(userVkId, LocalDateTime.now().minusDays(1));
            reviewStatistic.setCountOpenReviews(countOpenReviews);
            reviewStatistic.setCountReviewsPerDay(countCreatedReviewForLastDay);
            // Выполняется проверка, необходимо ли пользователю установить блок на создание ревью
            boolean needToBlock = false;
            if (maxReviewsWithoutStudentsInRow > 0 && reviewStatistic.getCountReviewsWithoutStudentsInRow() >= maxReviewsWithoutStudentsInRow) {
                needToBlock = true;
                reviewStatistic.setLastBlockReason(
                        String.format("Достигнуто максимальное количество идущих подряд принятых ревью, на которых не было ни одного студента.\nДата и время блокировки: %s.",
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                );
            } else if (maxOpenReviews > 0 && reviewStatistic.getCountOpenReviews() >= maxOpenReviews) {
                needToBlock = true;
                reviewStatistic.setLastBlockReason(
                        String.format("Достигнуто максимальное количество одновременно открытых ревью.\nДата и время блокировки: %s.",
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                );
            } else if (maxReviewsPerDay > 0 && reviewStatistic.getCountReviewsPerDay() >= maxReviewsPerDay) {
                needToBlock = true;
                reviewStatistic.setLastBlockReason(
                        String.format("Достигнуто максимальное количество проведенных ревью в сутки.\nДата и время блокировки: %s.",
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                );
            }
            if (needToBlock) {
                reviewStatistic.setReviewBlocked(true);
                reviewStatistic.setCountBlocks(reviewStatistic.getCountBlocks() + 1);
                logger.info("Пользователю (vkId={}) установлен блок на возможность создания ревью в связи с подозрением на фарм RP",
                        userVkId);
            }
            reviewStatisticDao.update(reviewStatistic);
        }

    }

    @Transactional
    @Override
    public void startReviewStatisticForUser(Integer userVkId) {
        Long countOpenReviews = reviewDao.getCountOpenReviewsByReviewerVkId(userVkId);
        Long countCreatedReviewForLastDay = reviewDao.getCountCompletedReviewsByReviewerVkIdFromDate(userVkId, LocalDateTime.now().minusDays(1));
        ReviewStatistic reviewStatistic = new ReviewStatistic();
        reviewStatistic.setUser(userDao.getByVkId(userVkId).get());
        reviewStatistic.setCountBlocks(0);
        reviewStatistic.setCountOpenReviews(countOpenReviews);
        reviewStatistic.setCountReviewsPerDay(countCreatedReviewForLastDay);
        reviewStatistic.setCountReviewsWithoutStudentsInRow((long)0);
        reviewStatisticDao.save(reviewStatistic);
    }

    @Override
    public Optional<ReviewStatistic> getReviewStatisticByUserVkId(Integer userVkId) {
        return reviewStatisticDao.getReviewStatisticByUserVkId(userVkId);
    }

    @Override
    public Optional<ReviewStatistic> getReviewStatisticByUserId(Long userId) {
        return reviewStatisticDao.getReviewStatisticByUserId(userId);
    }

    @Transactional
    @Override
    public void unblockTakingReviewForUser(Long userId) {
        Optional<ReviewStatistic> optionalReviewStatistic = reviewStatisticDao.getReviewStatisticByUserId(userId);
        ReviewStatistic reviewStatistic = optionalReviewStatistic.get();
        reviewStatistic.setCountReviewsWithoutStudentsInRow((long) 0);
        reviewStatistic.setReviewBlocked(false);
        reviewStatisticDao.update(reviewStatistic);
    }
}
