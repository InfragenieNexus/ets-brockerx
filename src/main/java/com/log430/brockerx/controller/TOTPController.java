package com.log430.brockerx.controller;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.util.TOTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TOTPController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/setup-totp")
    public String setupTOTP(Model model) throws Exception {
        User user = userRepository.findByUsername("william"); // exemple
        if (user.getTotpSecret() == null) {
            user.setTotpSecret(TOTPUtil.generateSecret());
            userRepository.save(user);
        }

        String qrCodeBase64 = TOTPUtil.generateQRCode(user.getUsername(), user.getTotpSecret());
        model.addAttribute("qrCode", qrCodeBase64);
        return "totp-setup";
    }

    @PostMapping("/verify-totp")
    public String verifyTOTP(@RequestParam int code, Model model) {
        User user = userRepository.findByUsername("william"); // exemple
        boolean valid = TOTPUtil.verifyCode(user.getTotpSecret(), code);
        model.addAttribute("valid", valid);
        return "totp-result";
    }
}

