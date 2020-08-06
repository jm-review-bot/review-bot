package spring.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@DiscriminatorValue(value = "2")
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
}
