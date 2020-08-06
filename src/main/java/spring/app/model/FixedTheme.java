package spring.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Getter
@Setter
@DiscriminatorValue(value = "1")
@Entity
@Table(name = "fixed_theme")
@PrimaryKeyJoinColumn(name = "theme_id")
public class FixedTheme extends Theme {

}
