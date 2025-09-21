package com.log430.brockerx.service;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public UserService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User createUser(User user) {
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

}
