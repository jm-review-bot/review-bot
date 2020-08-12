package spring.app.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/feedback")
public class FeedbackController {

    @GetMapping(value = "")
    public String index() {
        return "feedback";
    }
}
