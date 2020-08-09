package spring.app.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import spring.app.listener.ReviewListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(ReviewListener.class)
@Table(name = "review")
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "is_open")
    private Boolean isOpen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    public Review() {
    }

    public Review(User user, Theme theme, Boolean isOpen, LocalDateTime date) {
        this.user = user;
        this.theme = theme;
        this.isOpen = isOpen;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) &&
                Objects.equals(date, review.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
