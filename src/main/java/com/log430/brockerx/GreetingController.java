package com.log430.brockerx;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class GreetingController {

    @GetMapping("/")
    public String home() {
        return "index"; // WEB-INF/jsp/index.jsp
    }


}
