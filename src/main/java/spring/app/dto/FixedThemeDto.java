package spring.app.dto;

import javax.validation.constraints.NotBlank;

public class FixedThemeDto  extends ThemeDto {

    @NotBlank
    String type;

    public FixedThemeDto() {
        super();
        this.type = "fixed";
    }

    public FixedThemeDto(Long id,
                        String title,
                        Integer criticalWeight,
                        Integer position,
                        Integer reviewPoint) {
        super(id, title, criticalWeight, position, reviewPoint);
        this.type = "fixed";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
