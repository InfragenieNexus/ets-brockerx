package com.log430.brockerx.repository;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(Long userId);

    Wallet getWalletByUser(User user);
}
