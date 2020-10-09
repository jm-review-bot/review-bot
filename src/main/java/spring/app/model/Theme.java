package spring.app.model;

import lombok.Getter;
import lombok.Setter;
import spring.app.listener.ThemeListener;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.EntityListeners;
import java.util.Objects;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@EntityListeners(ThemeListener.class)
@DiscriminatorColumn(name = "theme_type")
@Table(name = "theme")
@Getter
@Setter
public abstract class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "critical_weight")
    private Integer criticalWeight;

    @Column(name = "review_point")
    private Integer reviewPoint;

    @Column(name = "position")
    private Integer position;

    @Column(name = "theme_type", nullable=false, updatable=false, insertable=false)
    private String themeType;

    public Theme() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return id.equals(theme.id) &&
                title.equals(theme.title) &&
                criticalWeight.equals(theme.criticalWeight) &&
                position.equals(theme.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, criticalWeight, position);
    }
}
