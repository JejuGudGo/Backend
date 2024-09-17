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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlannerReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("리뷰 - 목록")
    @Test
    @Disabled
    void getReviewList() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
        Pageable pageable = PageRequest.of(0, 10);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/reviews", 2L)
                        .header("Authorization", accessToken)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("리뷰 - 이미지 목록")
    @Test
    @Disabled
    void getImages() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
        Pageable pageable = PageRequest.of(0, 10);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/reviews/images", 2L)
                        .header("Authorization", accessToken)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("리뷰 - 상세")
    @Test
    @Disabled
    void getReview() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/reviews/{reviewId}", 2L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("리뷰 - 특정 이미지 조회")
    @Test
    @Disabled
    void getImage() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/reviews/{reviewId}/images/{imageId}", 2L, 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("리뷰 - 개수 조회")
    @Test
    @Disabled
    void getReviewCont() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/reviews/count", 2L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("리뷰 - 태그 반환")
    @Test
    @Disabled
    void getReviewTags() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/reviews/tags")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }
}
