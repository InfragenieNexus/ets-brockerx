package com.log430.brockerx.controller;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import com.log430.brockerx.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // ======= VIEW WALLET =======
    @GetMapping("/wallet/view") public String viewWallet(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user"); // récupère l'utilisateur connecté
        if (user == null) {
            return "redirect:/login"; // pas connecté → redirection
        }

        Wallet wallet = walletService.getWalletByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("wallet", wallet);

        model.addAttribute("contentPage", "wallet.jsp");
        return "layout";
    }


    // ======= DEPOSIT =======
    @PostMapping("/wallet/deposit") public String depositWallet(@RequestParam Long userId, @RequestParam Double amount,
                                                                @RequestHeader(value = "Idempotency-Key") String idempotencyKey,
                                                                Model model) {
        User user = walletService.getUserById(userId);
        Wallet wallet = walletService.getWalletByUser(user);

        walletService.deposit(userId, amount, idempotencyKey);

        model.addAttribute("user", user);
        model.addAttribute("wallet", wallet);

        model.addAttribute("contentPage", "wallet.jsp");
        return "layout";
    }
}
