package spring.app.dto;


public class FixedThemeDto  extends ThemeDto {

    public FixedThemeDto() {
        super();
    }

    public FixedThemeDto(Long id, String title, Integer criticalWeight, Integer position, Integer reviewPoint) {
        super(id, title, criticalWeight, position, reviewPoint);
    }

}
