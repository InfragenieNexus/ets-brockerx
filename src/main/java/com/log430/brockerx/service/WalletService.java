package com.log430.brockerx.service;

import com.log430.brockerx.entity.Transaction;
import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import com.log430.brockerx.repository.TransactionRepository;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(UserRepository userRepository, WalletRepository walletRepository,
                         TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional public Transaction deposit(Long userId, Double amount, String idempotencyKey) {

        Optional<Transaction> existingTx = transactionRepository.findByIdempotencyKey(idempotencyKey);
        if (existingTx.isPresent()) {
            return existingTx.get(); // on renvoie la transaction déjà existante
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(0.0);
            walletRepository.save(wallet);
        }

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setAmount(amount);
        tx.setStatus("Pending");
        tx.setIdempotencyKey(idempotencyKey);
        transactionRepository.save(tx);

        // Simuler le règlement immédiat
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
        tx.setStatus("Settled");
        transactionRepository.save(tx);

        return tx;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public Wallet getWalletByUser(User user) {
        if (user == null)
            return null;
        return walletRepository.findByUserId(user.getId());
    }
}
