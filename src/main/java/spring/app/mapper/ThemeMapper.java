package spring.app.mapper;


import org.mapstruct.Mapper;
import spring.app.dto.ThemeDto;
import spring.app.model.Theme;

@Mapper(componentModel = "spring")
public interface ThemeMapper {

    Theme themeDtoToThemeEntity(ThemeDto themeDto);

    ThemeDto themeEntityToThemeDto(Theme theme);
}
