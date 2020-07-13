package spring.app.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "free_theme")
public class FreeTheme extends Theme {

    @ManyToMany
    @JoinTable(
            name = "user_free_theme",
            joinColumns = {@JoinColumn(name = "free_theme_id")},
            inverseJoinColumns = {@JoinColumn(name = "examiner_id")}
    )
    List<User> examiners;

    public List<User> getExaminers() {
        return examiners;
    }

    public void setExaminers(List<User> examiners) {
        this.examiners = examiners;
    }
}
