package spring.app.mapper;


import org.mapstruct.Mapper;
import spring.app.dto.FixedThemeDto;
import spring.app.model.FixedTheme;

@Mapper(componentModel = "spring")
public interface ThemeMapper {

    FixedTheme fixedThemeDtoToFixedThemeEntity(FixedThemeDto fixedThemeDto);

    FixedThemeDto fixedThemeEntityToFixedThemeDto(FixedTheme fixedTheme);
}
