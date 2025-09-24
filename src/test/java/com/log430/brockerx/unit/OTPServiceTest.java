package com.log430.brockerx.unit;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.service.OTPService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OTPServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private OTPService otpService;

    @Test void generateOTP_shouldReturn6DigitCode() {
        User user = new User();
        user.setEmail("test@example.com");

        String code = otpService.generateOTP(user);

        assertNotNull(code);
        assertEquals(6, code.length());
    }

    @Test void verifyCode_shouldReturnTrueForCorrectCode() {
        User user = new User();
        user.setEmail("test@example.com");

        String code = otpService.generateOTP(user);

        assertTrue(otpService.verifyCode(user, code));
        assertFalse(otpService.verifyCode(user, "000000"));
    }

    @Test void sendOTP_shouldCallMailSender() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");

        // Ne renvoie rien, juste vérifier l'appel
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        otpService.sendOTP(user);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
