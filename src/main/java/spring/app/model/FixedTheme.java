package spring.app.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@DiscriminatorValue(value = "1")
@Entity
@Table(name = "fixed_theme")
public class FixedTheme extends Theme {

}
