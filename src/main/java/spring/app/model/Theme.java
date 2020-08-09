package spring.app.model;

import lombok.Getter;
import lombok.Setter;
import spring.app.listener.ThemeListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(ThemeListener.class)
@Table(name = "theme")
@Getter
@Setter
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
