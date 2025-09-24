package com.log430.brockerx.unit;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.entity.Wallet;
import com.log430.brockerx.repository.UserRepository;
import com.log430.brockerx.repository.WalletRepository;
import com.log430.brockerx.service.OTPService;
import com.log430.brockerx.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private OTPService otpService;

    @InjectMocks
    private UserService userService;

    @Test void signupCreatesUserAndWallet() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.signup("test@example.com", "password", "123456", "John", "Doe", "Addr", "2000-01-01");

        assertNotNull(result);
        verify(walletRepository, times(1)).save(any(Wallet.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test void signupThrowsExceptionWhenEmailExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(new User());

        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> userService.signup("test@example.com", "password", "123456", "John",
                                                                    "Doe", "Addr", "2000-01-01"));

        assertEquals("Email déjà utilisé", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test void activateUserWithOtpSuccess() {
        User user = new User();
        user.setStatus(User.Status.PENDING);

        when(otpService.verifyCode(user, "123456")).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.activateUserWithOtp(user, "123456"));
        assertEquals(User.Status.ACTIVE, user.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test void activateUserWithOtpFails() {
        User user = new User();
        user.setStatus(User.Status.PENDING);

        when(otpService.verifyCode(user, "wrong")).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> userService.activateUserWithOtp(user, "wrong"));

        assertEquals("Code invalide", exception.getMessage());
        assertEquals(User.Status.PENDING, user.getStatus());
        verify(userRepository, never()).save(user);
    }

    @Test void loginSuccess() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("password"));

        when(userRepository.findByEmail("user@test.com")).thenReturn(user);

        User result = userService.login("user@test.com", "password");
        assertNotNull(result);
        assertEquals("user@test.com", result.getEmail());
    }

    @Test void loginFailsWrongPassword() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("password"));

        when(userRepository.findByEmail("user@test.com")).thenReturn(user);

        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> userService.login("user@test.com", "wrong"));
        assertEquals("Email ou mot de passe incorrect", exception.getMessage());
    }

    @Test void loginFailsUnknownEmail() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> userService.login("unknown@test.com", "password"));
        assertEquals("Email ou mot de passe incorrect", exception.getMessage());
    }
}
