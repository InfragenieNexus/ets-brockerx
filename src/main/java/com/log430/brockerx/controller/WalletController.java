package com.log430.brockerx.controller;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import com.log430.brockerx.service.WalletService;
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

    @GetMapping("/wallet/view") public String viewWallet(@RequestParam Long userId, Model model) {
        // Appel du service pour récupérer l'utilisateur et son portefeuille
        User user = walletService.getUserById(userId);
        if (user == null) {
            model.addAttribute("error", "Utilisateur non trouvé");
            return "wallet"; // jsp wallet.jsp affichera l'erreur
        }

        Wallet wallet = walletService.getWalletByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("wallet", wallet);

        return "wallet"; // JSP : WEB-INF/jsp/wallet.jsp
    }

    @PostMapping("/wallet/deposit") public String depositWallet(@RequestParam Long userId, @RequestParam Double amount,
                                                                @RequestHeader(value = "Idempotency-Key") String idempotencyKey,
                                                                Model model) {
        User user = walletService.getUserById(userId);
        Wallet wallet = walletService.getWalletByUser(user);
        walletService.deposit(userId, amount, idempotencyKey);
        model.addAttribute("user", user);
        model.addAttribute("wallet", wallet);
        return "wallet";
    }
}
