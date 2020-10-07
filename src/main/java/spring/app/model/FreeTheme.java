package spring.app.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import java.util.List;

@DiscriminatorValue(value = "free")
@Entity
@Table(name = "free_theme")
@PrimaryKeyJoinColumn(name = "theme_id")
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
