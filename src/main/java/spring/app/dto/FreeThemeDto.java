package spring.app.dto;

import javax.validation.constraints.NotBlank;

public class FreeThemeDto extends ThemeDto {

    @NotBlank
    private final String type = "free";

    public String getType() {
        return type;
    }
}
