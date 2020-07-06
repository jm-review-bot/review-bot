package spring.app.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThemeView {
    @RequestMapping(value = "/admin/theme")
    public String index() {
        return "theme";
    }
}
