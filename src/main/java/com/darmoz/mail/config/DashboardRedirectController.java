package com.darmoz.mail.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardRedirectController {

    @GetMapping({"/dashboard", "/dashboard/"})
    String redirectToDashboard() {
        return "redirect:/dashboard/index.html";
    }
}
