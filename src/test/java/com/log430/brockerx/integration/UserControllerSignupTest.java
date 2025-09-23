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

    @Test void testSignupSuccess() throws Exception {
        mockMvc.perform(post("/signup").param("email", "user@test.com").param("password", "pass123").param("phone",
                                                                                                           "1234567890")
                                       .param("firstName", "William").param("lastName", "Desgagné")
                                       .param("address", "123 Rue Test").param("dateOfBirth", "2000-01-01")).andExpect(
                status().is3xxRedirection()).andExpect(redirectedUrl("/verify-otp"));
    }
}

