package com.log430.brockerx.service;

import com.log430.brockerx.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {

    @Autowired
    private JavaMailSender mailSender;

    // Stockage temporaire des codes OTP en mémoire (email -> code)
    private final Map<String, String> otpStorage = new HashMap<>();

    /**
     * Génère un code OTP aléatoire à 6 chiffres et le stocke pour l'utilisateur.
     */
    public String generateOTP(User user) {
        String code = String.valueOf((int) (Math.random() * 900000) + 100000);
        otpStorage.put(user.getEmail(), code);
        return code;
    }

    /**
     * Vérifie si le code fourni correspond à celui stocké pour l'utilisateur.
     */
    public boolean verifyCode(User user, String code) {
        String stored = otpStorage.get(user.getEmail());
        return stored != null && stored.equals(code);
    }

    /**
     * Envoie le code OTP par email.
     */
    public void sendOTP(User user) {
        String code = generateOTP(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Votre code OTP BrokerX");
        message.setText("Bonjour " + user.getFirstName() + ",\n\nVotre code OTP est : " + code +
                        "\n\nCe code est valide pour 10 minutes.");

        mailSender.send(message);

        System.out.println("Code OTP envoyé à " + user.getEmail() + " : " + code); // pour test console
    }
}
