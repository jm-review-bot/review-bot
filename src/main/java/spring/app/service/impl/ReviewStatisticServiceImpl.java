package spring.app.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.dao.abstraction.ReviewDao;
import spring.app.dao.abstraction.ReviewStatisticDao;
import spring.app.dao.abstraction.UserDao;
import spring.app.model.ReviewStatistic;
import spring.app.service.abstraction.ReviewStatisticService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReviewStatisticServiceImpl implements ReviewStatisticService {

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

    @Override
    public void updateReviewStatistic(ReviewStatistic reviewStatistic) {
        reviewStatisticDao.update(reviewStatistic);
    }

    @Override
    public ReviewStatistic getReviewStatisticByUserVkId(Integer userVkId) {
        return reviewStatisticDao.getReviewStatisticByUserVkId(userVkId);
    }

    @Override
    public ReviewStatistic getReviewStatisticByUserId(Long userId) {
        return reviewStatisticDao.getReviewStatisticByUserId(userId);
    }

    /* Метод обновляет существующую статистику по ревью выбранного пользователя на основании текущих данных.
    * Если таковой на момент проверки еще не было заведено, то она будет создана.
    *
    * @return ReviewStatistic - метод возвращает актуализированную статистику по пользователю*/
    @Transactional
    @Override
    public ReviewStatistic updateStatisticForUser(Integer userVkId) {
        ReviewStatistic reviewStatistic = reviewStatisticDao.getReviewStatisticByUserVkId(userVkId);
        Long countOpenReviews = reviewDao.getCountOpenReviewsByReviewerVkId(userVkId);
        Long countCreatedReviewForLastDay = reviewDao.getCountCompletedReviewsByReviewerVkIdFromDate(userVkId, LocalDateTime.now().minusDays(1));
        if (reviewStatistic != null) { // Если статистика по пользователю уже ведется
            if (!reviewStatistic.isReviewBlocked()) { // И если пользователю доступ к принятию ревью не заблокирован
                reviewStatistic.setCountOpenReviews(countOpenReviews);
                reviewStatistic.setCountReviewsPerDay(countCreatedReviewForLastDay);
                reviewStatisticDao.update(reviewStatistic);
            }
        } else { // Если статистика по пользователю еще не велась, необходимо начать ее
            reviewStatistic = new ReviewStatistic();
            reviewStatistic.setUser(userDao.getByVkId(userVkId));
            reviewStatistic.setCountBlocks(0);
            reviewStatistic.setCountOpenReviews(countOpenReviews);
            reviewStatistic.setCountReviewsPerDay(countCreatedReviewForLastDay);
            reviewStatistic.setCountReviewsWithoutStudentsInRow((long)0);
            reviewStatisticDao.save(reviewStatistic);
        }
        /* Если пользователю доступ к принятию ревью не заблокирован, то после актулизации статистики по нему необходимо
         * проверить все заданные ограничения для создания нового ревью во избежания фарма RP пользователем */
        if (!reviewStatistic.isReviewBlocked()) {
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
                        String.format("Достигнуто максимальное количество проводимых ревью в сутки.\nДата и время блокировки: %s.",
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                );
            }
            /* Актуализировав данные, выполняется проверка текущего статуса блокировки и изменяется при необходимости.
             * Здесь не происходит автоматическая разблокировка пользователя (например, если пользователь удалит созданные ревью),
             * поскольку факт блокировки должен быть зафиксирован админом, и только он может производить разблокировку пользователя*/
            if (needToBlock && !reviewStatistic.isReviewBlocked()) {
                reviewStatistic.setReviewBlocked(true);
                reviewStatistic.setCountBlocks(reviewStatistic.getCountBlocks() + 1);
                reviewStatisticDao.update(reviewStatistic);
            }
        }
        return reviewStatistic;
    }
}
