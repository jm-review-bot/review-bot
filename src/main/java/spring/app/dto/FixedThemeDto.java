package spring.app.dto;


public class FixedThemeDto extends ThemeDto {

    public FixedThemeDto() {
        super();
    }

    public FixedThemeDto(Long version, Long id, String title, Integer criticalWeight, Integer position, Integer reviewPoint) {
        super(version, id, title, criticalWeight, position, reviewPoint);
    }

}
