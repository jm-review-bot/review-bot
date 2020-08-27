package spring.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "review_statistic")
@Getter
@Setter
public class ReviewStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "is_review_blocked")
    private boolean isReviewBlocked;

    // Причина последней блокировки аккаунта
    @Column(name = "last_block_reason")
    private String lastBlockReason;

    // Количетво блокировок аккаунта на создание ревью
    @NotNull
    @Column(name = "count_blocks")
    private int countBlocks;

    // Количество ревью, идущих подряд, которые провел пользователь, но на которые никто не записался
    @NotNull
    @Column(name = "reviews_without_students_in_row")
    private Long countReviewsWithoutStudentsInRow;

    // Количество открытых ревью пользователя одновременно
    @NotNull
    @Column(name = "open_reviews")
    private Long countOpenReviews;

    // Количество проведенных ревью за последние сутки
    @NotNull
    @Column(name = "reviews_per_day")
    private Long countReviewsPerDay;

    public ReviewStatistic() {
    }
}
