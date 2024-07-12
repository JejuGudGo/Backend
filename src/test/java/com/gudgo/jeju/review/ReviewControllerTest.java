package com.gudgo.jeju.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("리뷰 - 태그 반환")
    @Test
    @Disabled
    void getReviewTags() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/reviews/categories/tags")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }
}
