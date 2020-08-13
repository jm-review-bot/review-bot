package spring.app.dto;

import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

public class FixedThemeDto  extends ThemeDto {

    @Null(groups=UpdateGroup.class)
    @NotNull(groups= CreateGroup.class)
    private String type;

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
