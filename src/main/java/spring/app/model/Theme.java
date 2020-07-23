package spring.app.model;

import spring.app.listener.ThemeListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(ThemeListener.class)
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "criticalWeight")
    private Integer criticalWeight;

    @Column(name = "position")
    private Integer position;

    @Column(name = "reviewPoint")
    private Integer reviewPoint;

    public Theme() {
    }

    public Integer getCriticalWeight() {
        return criticalWeight;
    }

    public void setCriticalWeight(Integer criticalWeight) {
        this.criticalWeight = criticalWeight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getReviewPoint() {
        return reviewPoint;
    }

    public void setReviewPoint(Integer reviewPoint) {
        this.reviewPoint = reviewPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return id.equals(theme.id) &&
                title.equals(theme.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
