package com.log430.brockerx.controller;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.service.OTPService;
import com.log430.brockerx.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final OTPService otpService;
    private final UserService userService;

    public UserController(OTPService otpService, UserService userService) {
        this.otpService = otpService;
        this.userService = userService;
    }

    // ======= LOGIN =======
    @GetMapping("/login") public String showLoginForm(Model model) {
        model.addAttribute("contentPage", "login.jsp");
        return "layout";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        try {
            User user = userService.login(email, password);

            if (user.isMfaEnabled()) {
                session.setAttribute("userPendingMfa", user);
                return "redirect:/verify-totp";
            }

            session.setAttribute("user", user);
            return "redirect:/wallet/view?userId=" + user.getId();

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("contentPage", "login.jsp");
            return "layout";
        }
    }

    // ======= SIGNUP =======
    @GetMapping("/signup") public String showSignupForm(Model model) {
        model.addAttribute("contentPage", "signup.jsp");
        return "layout";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String email, @RequestParam String password, @RequestParam String phone,
                         @RequestParam String firstName, @RequestParam String lastName, @RequestParam String address,
                         @RequestParam String dateOfBirth, HttpSession session, Model model) {
        try {
            User user = userService.signup(email, password, phone, firstName, lastName, address, dateOfBirth);
            session.setAttribute("userPending", user);
            otpService.sendOTP(user);
            return "redirect:/verify-otp";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("contentPage", "signup.jsp");
            return "layout";
        }
    }

    // ======= VERIFY OTP =======
    @GetMapping("/verify-otp") public String showVerifyOtpForm(Model model) {
        model.addAttribute("contentPage", "verify-otp.jsp");
        return "layout";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otpCode, HttpSession session, Model model) {
        User user = (User) session.getAttribute("userPending");
        if (user == null) {
            return "redirect:/signup";
        }

        try {
            userService.activateUserWithOtp(user, otpCode); // logique métier dans service
            session.setAttribute("user", user); // session active
            return "redirect:/wallet/view?userId=" + user.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("contentPage", "verify-otp.jsp");
            return "layout";
        }
    }
}
