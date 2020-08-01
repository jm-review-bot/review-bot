package spring.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@DiscriminatorValue(value = "free")
@Entity
@Table(name = "free_theme")
@PrimaryKeyJoinColumn(name = "theme_id")
public class FreeTheme extends Theme {

    @NotBlank
    @Transient
    private String type;

    @ManyToMany
    @JoinTable(
            name = "user_free_theme",
            joinColumns = {@JoinColumn(name = "free_theme_id")},
            inverseJoinColumns = {@JoinColumn(name = "examiner_id")}
    )
    List<User> examiners;

    public FreeTheme() {
        super();
        this.type = "free";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<User> getExaminers() {
        return examiners;
    }

    public void setExaminers(List<User> examiners) {
        this.examiners = examiners;
    }
}
