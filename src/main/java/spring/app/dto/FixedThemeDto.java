package spring.app.dto;

import javax.validation.constraints.NotBlank;

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
