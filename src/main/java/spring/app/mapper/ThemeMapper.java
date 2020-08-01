package spring.app.mapper;


import org.mapstruct.Mapper;
import spring.app.dto.FixedThemeDto;
import spring.app.dto.FreeThemeDto;
import spring.app.dto.ThemeDto;
import spring.app.model.FixedTheme;
import spring.app.model.FreeTheme;
import spring.app.model.Theme;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ThemeMapper {

    FixedTheme fixedThemeDtoToFixedThemeEntity(FixedThemeDto fixedThemeDto);

    FixedThemeDto fixedThemeEntityToFixedThemeDto(FixedTheme fixedTheme);

    FreeTheme freeThemeDtoToFreeThemeEntity(FreeThemeDto freeThemeDto);

    FreeThemeDto freeThemeEntityToFreeThemeDto(FreeTheme freeTheme);

    Theme themeDtoToThemeEntity(ThemeDto themeDto);

    ThemeDto themeEntityToThemeDto(Theme theme);
}
