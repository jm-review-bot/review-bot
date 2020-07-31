package spring.app.dto;

import javax.validation.constraints.NotBlank;

public class FreeThemeDto extends ThemeDto {

    @NotBlank
    String type;

    public FreeThemeDto() {
        super();
        this.type = "free";
    }

    public FreeThemeDto(Long id,
                        String title,
                        Integer criticalWeight,
                        Integer position,
                        Integer reviewPoint) {
        super(id, title, criticalWeight, position, reviewPoint);
        this.type = "free";
    }
}
