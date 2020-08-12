package spring.app.dto;

import javax.validation.constraints.NotBlank;

public class FreeThemeDto extends ThemeDto {

    @NotBlank
    private final String type = "free";

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
