package com.log430.brockerx.unit;

import com.log430.brockerx.entity.Transaction;
import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import com.log430.brockerx.repository.TransactionRepository;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.repository.WalletRepository;
import com.log430.brockerx.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WalletService walletService;

    @Test void depositCreatesTransactionAndUpdatesWallet() {
        Long userId = 1L;
        Double amount = 100.0;
        String key = "unique-key";

        User mockUser = new User();
        mockUser.setId(userId);

        Wallet mockWallet = new Wallet();
        mockWallet.setUser(mockUser);
        mockWallet.setBalance(50.0);

        // Simuler les appels repo
        when(transactionRepository.findByIdempotencyKey(key)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(walletRepository.findByUserId(userId)).thenReturn(mockWallet);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        // Appel de la méthode
        Transaction tx = walletService.deposit(userId, amount, key);

        // Vérifications
        assertNotNull(tx);
        assertEquals("Settled", tx.getStatus());
        assertEquals(amount, tx.getAmount());
        assertEquals(mockUser, tx.getUser());
        assertEquals(150.0, mockWallet.getBalance());

        verify(transactionRepository, times(2)).save(any(Transaction.class)); // creation + status updated
        verify(walletRepository, times(1)).save(mockWallet);
    }

    @Test void depositReturnsExistingTransactionIfIdempotencyKeyExists() {
        Long userId = 1L;
        Double amount = 100.0;
        String key = "existing-key";

        Transaction existingTx = new Transaction();
        existingTx.setIdempotencyKey(key);

        when(transactionRepository.findByIdempotencyKey(key)).thenReturn(Optional.of(existingTx));

        Transaction tx = walletService.deposit(userId, amount, key);

        assertEquals(existingTx, tx);
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test void getUserByIdReturnsUser() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = walletService.getUserById(userId);

        assertEquals(mockUser, result);
    }

    @Test void getWalletByUserReturnsWallet() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        Wallet mockWallet = new Wallet();
        mockWallet.setUser(mockUser);

        when(walletRepository.findByUserId(userId)).thenReturn(mockWallet);

        Wallet result = walletService.getWalletByUser(mockUser);

        assertEquals(mockWallet, result);
    }

    @Test void getWalletByUserReturnsNullIfUserIsNull() {
        assertNull(walletService.getWalletByUser(null));
    }
}
