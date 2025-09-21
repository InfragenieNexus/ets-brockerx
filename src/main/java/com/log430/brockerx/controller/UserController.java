package com.log430.brockerx.controller;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showForm() {
        return "createUser"; // correspond à createUser.jsp
    }

    @PostMapping("/save")
    public String saveUser(@RequestParam String username, @RequestParam String password) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setEmail(username);
        userService.createUser(u);
        return "redirect:/users/new";
    }
    @GetMapping("/all")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users"; // JSP: /WEB-INF/jsp/users.jsp
    }
}

