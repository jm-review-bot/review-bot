package spring.app.dto;

import lombok.Getter;
import lombok.Setter;
import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FixedThemeDto  extends ThemeDto {

    @NotBlank
    private final String type = "fixed";

    public FixedThemeDto() {
        super();
    }

    public FixedThemeDto(Long id, String title, Integer criticalWeight, Integer position, Integer reviewPoint) {
        super(id, title, criticalWeight, position, reviewPoint);
    }

    public String getType() {
        return type;
    }
}
