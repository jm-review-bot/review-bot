package spring.app.viewController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/theme")
@Api(value = "Admin theme page")
public class AdminController {

    @GetMapping(value = "")
    @ApiOperation(value = "Show theme page", response = String.class)
    public String index() {
        return "theme";
    }
}
