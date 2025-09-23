package com.log430.brockerx.unit;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.repository.WalletRepository;
import com.log430.brockerx.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private UserService userService;

    @Test void signupCreatesUserAndWallet() {
        // Création d'un User simulé
        User mockUser = new User();
        mockUser.setEmail("test@example.com");

        // Quand userRepository.save est appelé, retourne mockUser
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Appel de la méthode à tester
        User result = userService.signup("test@example.com", "password", "123456", "John", "Doe", "Addr", "2000-01-01");

        // Vérifie que le résultat n'est pas nul
        assertNotNull(result);

        // Vérifie que le wallet a été sauvegardé
        verify(walletRepository, times(1)).save(any(Wallet.class));

        // Vérifie que le user a été sauvegardé
        verify(userRepository, times(1)).save(any(User.class));
    }
}


