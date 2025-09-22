package com.log430.brockerx.controller;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.service.OTPService;
import com.log430.brockerx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OTPService otpService;

    public UserController(UserService userService) {
        this.userService = userService;
        this.otpService = new OTPService();
    }


    @GetMapping("/all") public String allUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users"; // JSP: /WEB-INF/jsp/users.jsp
    }


}

