package spring.app.service.abstraction;

import spring.app.model.Theme;

import java.util.List;

public interface ThemeService {

    void addTheme(Theme theme);

    Theme getThemeById(Long id);

    List<Theme> getAllThemes();

    void updateTheme(Theme theme);

    void deleteThemeById(Long id);

    Theme getByPosition(Integer position);

}
