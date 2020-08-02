package spring.app.mapper;


import org.mapstruct.Mapper;
import spring.app.dto.FixedThemeDto;
import spring.app.dto.FreeThemeDto;
import spring.app.dto.ThemeDto;
import spring.app.model.FixedTheme;
import spring.app.model.FreeTheme;
import spring.app.model.Theme;

@Mapper(componentModel = "spring")
public interface ThemeMapper {

    FixedTheme fixedThemeDtoToFixedThemeEntity(ThemeDto themeDto);

    FixedThemeDto fixedThemeEntityToFixedThemeDto(Theme theme);

    FreeTheme freeThemeDtoToFreeThemeEntity(ThemeDto themeDto);

    FreeThemeDto freeThemeEntityToFreeThemeDto(Theme theme);
}
