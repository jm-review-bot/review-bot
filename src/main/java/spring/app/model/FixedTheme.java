package spring.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fixed_theme")
public class FixedTheme extends Theme {

    @Column(name = "reviewPoint")
    private Integer reviewPoint;

    public Integer getReviewPoint() {
        return reviewPoint;
    }

    public void setReviewPoint(Integer reviewPoint) {
        this.reviewPoint = reviewPoint;
    }
}
