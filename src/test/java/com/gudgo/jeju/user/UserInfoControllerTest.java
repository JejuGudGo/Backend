package com.gudgo.jeju.user;

import com.gudgo.jeju.domain.user.service.UserInfoService;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private SubjectExtractor subjectExtractor;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TokenGenerator tokenGenerator;


    @DisplayName("유저 정보 반환")
    @Test
    @Disabled
    void getUserInfo() throws Exception {
        String accessToken = "Bearer "+ tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/auth/user")
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());
    }
}
