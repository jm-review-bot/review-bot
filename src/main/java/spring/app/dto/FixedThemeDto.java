package spring.app.dto;

import javax.validation.constraints.NotBlank;

public class FixedThemeDto  extends ThemeDto {

    @NotBlank
    private final String type = "fixed";

    public String getType() {
        return type;
    }
}
