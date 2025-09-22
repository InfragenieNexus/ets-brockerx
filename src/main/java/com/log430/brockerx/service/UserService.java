package com.log430.brockerx.service;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final OTPService otpService;

    public UserService(UserRepository userRepository, WalletRepository walletRepository, OTPService otpService) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.otpService = otpService;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional public User createUser(User user) {
        User savedUser = userRepository.save(user);

        // Créer un portefeuille vide associé à cet utilisateur
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(0.0); // solde initial
        walletRepository.save(wallet);

        return savedUser;
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User signup(String email, String password, String phone, String firstName, String lastName, String address,
                       String dateOfBirth) {

        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setDateOfBirth(LocalDate.parse(dateOfBirth));
        user.setStatus(User.Status.PENDING);

        return this.createUser(user);
    }

    @Transactional public void activateUserWithOtp(User user, String otpCode) {
        if (!otpService.verifyCode(user, otpCode)) {
            throw new IllegalArgumentException("Code invalide");
        }

        user.setStatus(User.Status.ACTIVE);
        userRepository.save(user);
    }
    
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }
        return user;
    }

}
