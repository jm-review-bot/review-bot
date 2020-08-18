package spring.app.dto;


public class FreeThemeDto extends ThemeDto {

    public FreeThemeDto() {
        super();
    }

    public FreeThemeDto(Long id, String title, Integer criticalWeight, Integer position, Integer reviewPoint) {
        super(id, title, criticalWeight, position, reviewPoint);
    }

}
