package com.log430.brockerx.integration;

import com.log430.brockerx.service.OTPService;
import com.log430.brockerx.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // H2 en mémoire
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private OTPService otpService;

    @TestConfiguration
    static class TestConfig {

    }

    @Test void testSignupFlow() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/signup").param("email", "user@test.com").param("password", "pass123").param("phone",
                                                                                                           "1234567890")
                                       .param("firstName", "William").param("lastName", "Desgagné").param("address",
                                                                                                          "123 Rue Test")
                                       .param("dateOfBirth", "2000-01-01").session(session)).andExpect(
                status().is3xxRedirection()).andExpect(redirectedUrl("/verify-otp"));

        Object userPending = session.getAttribute("userPending");
        assert userPending != null;
    }

    @Test void testVerifyOtpFlow() throws Exception {
        MockHttpSession session = new MockHttpSession();

        var user = userService.signup("user2@test.com", "pass123", "1234567890", "William", "Desgagné", "123 Rue Test",
                                      "2000-01-01");
        session.setAttribute("userPending", user);

        String code = otpService.generateOTP(user);

        mockMvc.perform(post("/verify-otp").param("otpCode", code).session(session)).andExpect(
                status().is3xxRedirection()).andExpect(redirectedUrl("/wallet/view?userId=" + user.getId()));

        Object activeUser = session.getAttribute("user");
        assert activeUser != null;
    }
}
