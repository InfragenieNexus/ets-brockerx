package com.log430.brockerx.service;

import com.log430.brockerx.dto.DepositResponseDto;
import com.log430.brockerx.entity.Transaction;
import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import com.log430.brockerx.repository.TransactionRepository;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.repository.WalletRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WalletService {

    private static final double MARGE = 100;

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;


    public WalletService(UserRepository userRepository, WalletRepository walletRepository,
                         TransactionRepository transactionRepository, UserService userService) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional public DepositResponseDto deposit(Long userId, Double amount, String idempotencyKey) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Wallet wallet = walletRepository.findByUserId(userId);

        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(0.0);
            walletRepository.save(wallet);
        }

        Optional<Transaction> existingTx = transactionRepository.findByIdempotencyKey(idempotencyKey);
        if (existingTx.isPresent()) {

            return new DepositResponseDto(wallet.getBalance());
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


        return new DepositResponseDto(wallet.getBalance());

    }


    public Wallet getWalletByUser(User user) {
        if (user == null)
            return null;
        return walletRepository.findByUserId(user.getId());
    }

    public boolean checkWalletFunds(Long userId, double amount) {

        User user = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Wallet wallet = this.walletRepository.getWalletByUser(user);

        return wallet.getBalance() >= amount + MARGE;
    }

    public void debit(Long userId, Double amount) {
        Wallet wallet = walletRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        wallet.setBalance(wallet.getBalance() - amount);
    }

    public void credit(Long userId, Double amount) {
        Wallet wallet = walletRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        wallet.setBalance(wallet.getBalance() + amount);
    }
}
