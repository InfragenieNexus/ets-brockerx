package com.log430.brockerx.integration;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.service.OTPService;
import com.log430.brockerx.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerSignupTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OTPService otpService;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean public OTPService otpService() {
            return Mockito.mock(OTPService.class);
        }

        @Bean public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test void testSignupSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("user@test.com");

        when(userService.signup(any(), any(), any(), any(), any(), any(), any())).thenReturn(mockUser);

        doNothing().when(otpService).sendOTP(any(User.class));

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/signup").param("email", "user@test.com").param("password", "pass123").param("phone",
                                                                                                           "1234567890")
                                       .param("firstName", "William").param("lastName", "Desgagné").param("address",
                                                                                                          "123 Rue Test")
                                       .param("dateOfBirth", "2000-01-01").session(session)).andExpect(
                status().is3xxRedirection()).andExpect(redirectedUrl("/verify-otp"));

        User sessionUser = (User) session.getAttribute("userPending");
        assert sessionUser != null;
        assert sessionUser.getEmail().equals("user@test.com");
    }

    @Test void testSignupFailure() throws Exception {
        when(userService.signup(any(), any(), any(), any(), any(), any(), any())).thenThrow(
                new IllegalArgumentException("Email déjà utilisé"));

        mockMvc.perform(post("/signup").param("email", "duplicate@test.com").param("password", "pass123").param("phone",
                                                                                                                "1234567890")
                                       .param("firstName", "William").param("lastName", "Desgagné")
                                       .param("address", "123 Rue Test").param("dateOfBirth", "2000-01-01")).andExpect(
                status().isOk()).andExpect(model().attributeExists("error")).andExpect(
                model().attribute("error", "Email déjà utilisé")).andExpect(
                model().attribute("contentPage", "signup.jsp")).andExpect(view().name("layout"));
    }
}
