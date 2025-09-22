package com.log430.brockerx.controller;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.service.OTPService;
import com.log430.brockerx.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class LoginController {


    private final OTPService otpService;
    private final UserService userService;


    public LoginController(OTPService otpService, UserService userService) {
        this.otpService = otpService;
        this.userService = userService;
    }

    @GetMapping("/signup") public String showSignupForm() {
        return "signup";
    }

    @GetMapping("/login") public String showLoginForm() {
        return "login";
    }


    @PostMapping("/signup")
    public String signup(@RequestParam String email, @RequestParam String password, @RequestParam String phone,
                         @RequestParam String firstName, @RequestParam String lastName, @RequestParam String address,
                         @RequestParam String dateOfBirth, HttpSession session, Model model) {

        if (userService.findByEmail(email) != null) {
            model.addAttribute("error", "Email déjà utilisé");
            return "signup";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setPhone(phone);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setDateOfBirth(LocalDate.parse(dateOfBirth));
        user.setStatus(User.Status.PENDING);

        userService.createUser(user);

        session.setAttribute("userPending", user);
        // Envoyer OTP par email/SMS
        otpService.sendOTP(user);

        return "redirect:/verify-otp";
    }

    @GetMapping("/verify-otp") String showVerifyOtpForm() {
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otpCode, HttpSession session, Model model) {
        User user = (User) session.getAttribute("userPending");
        if (user == null)
            return "redirect:/signup";

        boolean valid = otpService.verifyCode(user, otpCode);
        if (!valid) {
            model.addAttribute("error", "Code invalide");
            return "verify-otp";
        }

        // Activation du compte
        user.setStatus(User.Status.ACTIVE);
        userService.save(user);


        session.setAttribute("user", user); // session active
        return "redirect:/wallet/view?userId=" + user.getId();
    }

}

