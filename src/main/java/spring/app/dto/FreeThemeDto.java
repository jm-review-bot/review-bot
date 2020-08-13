package spring.app.dto;

import spring.app.groups.CreateGroup;
import spring.app.groups.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class FreeThemeDto extends ThemeDto {

    @Null(groups= UpdateGroup.class)
    @NotNull(groups= CreateGroup.class)
    private String type;

    public FreeThemeDto() {
        super();
    }

    public FreeThemeDto(Long id, String title, Integer criticalWeight, Integer position, Integer reviewPoint) {
        super(id, title, criticalWeight, position, reviewPoint);
    }

    public String getType() {
        return type;
    }
}
