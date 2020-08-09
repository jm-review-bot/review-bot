package spring.app.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@DiscriminatorValue(value = "fixed")
@Entity
@Table(name = "fixed_theme")
@PrimaryKeyJoinColumn(name = "theme_id")
public class FixedTheme extends Theme {

}
