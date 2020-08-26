package spring.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    @NotNull
    @Column(name = "block_reason")
    private String blockReason;

    @NotNull
    @Column(name = "count_blocks")
    private int countBlocks;

    @NotNull
    @Column(name = "reviews_without_students_in_row")
    private int countReviewsWithoutStudentsInRow;

    @NotNull
    @Column(name = "open_reviews")
    private int countOpenReviews;

    @NotNull
    @Column(name = "reviews_per_day")
    private int countReviewsPerDay;

    public ReviewStatistic() {
    }
}
