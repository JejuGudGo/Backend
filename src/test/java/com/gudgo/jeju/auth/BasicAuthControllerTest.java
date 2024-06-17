package com.gudgo.jeju.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.global.auth.basic.dto.request.LoginRequest;
import com.gudgo.jeju.global.auth.basic.dto.request.SignupRequest;
import com.gudgo.jeju.global.auth.basic.service.LoginService;
import com.gudgo.jeju.global.auth.basic.service.SignupService;
import com.gudgo.jeju.global.util.CookieUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BasicAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SignupService signupService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private CookieUtil cookieUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("회원 가입 - 정상적인 값으로 요청")
    @Test
    @Disabled
    void signup() throws Exception {
        SignupRequest request = new SignupRequest(
                "test1@naver.com",
                "123qwe!@#WQE",
                "시은",
                "김시은",
                "01012345678"
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @DisplayName("회원 가입 - 비정상적인 값으로 요청")
    @Test
    @Disabled
    void signupIllegalValue() throws Exception {
        SignupRequest request = new SignupRequest(
                "test1@naver.com",
                "123q",
                "시은",
                "김시은",
                "010-6553-5378"
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("로그인 - 정상적인 값으로 요청")
    @Test
    @Disabled
    void login() throws Exception {
        LoginRequest request = new LoginRequest(
                "test1@naver.com",
                "123qwe!@#WQE"
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @DisplayName("로그인 - 비정상적인 값으로 요청")
    @Test
    @Disabled
    void loginIllegalValue() throws Exception {
        LoginRequest request = new LoginRequest(
                "test1@naver.com",
                "123qwe"
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("로그아웃")
    @Test
    @Disabled
    void logout() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
