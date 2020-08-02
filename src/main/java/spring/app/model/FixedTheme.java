package spring.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@DiscriminatorValue(value = "fixed")
@Entity
@Table(name = "fixed_theme")
@PrimaryKeyJoinColumn(name = "theme_id")
public class FixedTheme extends Theme {

    @NotBlank
    @Transient
    private String type;

    public FixedTheme() {
        super();
        this.type = "fixed";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
