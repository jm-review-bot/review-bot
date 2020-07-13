package spring.app.viewController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/theme")
@Api(value = "Представление станицы тем и вопросов")
public class AdminController {

    @GetMapping(value = "")
    @ApiOperation(value = "Просмотр станицы", response = String.class)
    public String index() {
        return "theme";
    }
}
