package spring.app.dto;


public class FreeThemeDto extends ThemeDto {

    public FreeThemeDto() {
        super();
    }

    public FreeThemeDto(Long version, Long id, String title, Integer criticalWeight, Integer position, Integer reviewPoint) {
        super(version, id, title, criticalWeight, position, reviewPoint);
    }

}
