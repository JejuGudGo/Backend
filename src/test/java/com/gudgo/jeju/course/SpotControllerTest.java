package com.gudgo.jeju.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotCreateRequestDto;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotCreateUsingApiRequest;
import com.gudgo.jeju.domain.planner.spot.service.SpotService;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
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
public class SpotControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpotService spotService;

    @DisplayName("스팟 리스트 반환")
    @Test
    @Disabled
    void getSpots() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/user/{userId}/planners/{plannerId}/course/spots", 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("특정 스팟 반환")
    @Test
    @Disabled
    void getSpot() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/user/{userId}/planners/{plannerId}/course/spots/{spotId}", 1L, 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("새로운 스팟 생성")
    @RepeatedTest(5)
    @Disabled
    void createSpotByUser() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        SpotCreateRequestDto requestDto = new SpotCreateRequestDto("Spot Title", "Spot Address", "A01",123.456, 78.910);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/user/{userId}/planners/{plannerId}/course/spots/{spotId}", 1L, 11L, 21L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("API를 사용하여 스팟 생성")
    @Test
    @Disabled
    void createSpotUsingTourApi() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        SpotCreateUsingApiRequest request = new SpotCreateUsingApiRequest("38","1013246");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/user/{userId}/planners/{plannerId}/course/spots/tour", 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("특정 스팟 삭제")
    @Test
    @Disabled
    void deleteSpot() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/v1/userId/{userId}/planners/{plannerId}/course/spots/{spotId}", 1L, 1L, 1L)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("특정 스팟 완료 처리")
    @Test
    @Disabled
    void completeSpot() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/v1/user/courses/{courseId}/spots/{spotId}/status", 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("특정 스팟의 선택 횟수 증가")
    @Test
    @Disabled
    void increaseCount() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/v1/spots/{spotId}/count", 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
}
