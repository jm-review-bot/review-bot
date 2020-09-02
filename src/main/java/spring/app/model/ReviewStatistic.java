package spring.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter
@Setter
public class ReviewStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column
    private boolean isReviewBlocked;

    // Причина последней блокировки аккаунта
    @Column
    private String lastBlockReason;

    // Количетво блокировок аккаунта на создание ревью
    @NotNull
    @Column
    private int countBlocks;

    // Количество ревью, идущих подряд, которые провел пользователь, но на которые никто не записался
    @NotNull
    @Column
    private Long countReviewsWithoutStudentsInRow;

    // Количество открытых ревью пользователя одновременно
    @NotNull
    @Column
    private Long countOpenReviews;

    // Количество проведенных ревью за последние сутки
    @NotNull
    @Column
    private Long countReviewsPerDay;

    public ReviewStatistic() {
    }
}
