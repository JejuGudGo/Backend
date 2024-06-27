package com.gudgo.jeju.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.planner.service.ParticipantService;
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
public class ParticipantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ParticipantService participantService;

    @DisplayName("참가자 리스트 반환 - 승낙")
    @Disabled
    @Test
    void getApprovedParticipantList() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/participants", 1L)
                        .header("Authorization", accessToken)
                        .param("status", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("참가자 리스트 반환 - 미승인")
    @Disabled
    @Test
    void getUnApprovedParticipantList() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/participants", 1L)
                        .header("Authorization", accessToken)
                        .param("status", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("참가 신청")
    @Disabled
    @Test
    void requestJoin() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/participants/join/{userId}", 1L, 1L)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("참가 취소")
    @Disabled
    @Test
    void cancelJoin() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/participants/cancel/{userId}", 1L, 1L)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("참가 신청 유저 - 승낙")
    @Disabled
    @Test
    void approveUser() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/participants/approve/{userId}", 1L, 1L)
                        .header("Authorization", accessToken)
                        .param("status", "true"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("참가 신청 유저 - 거절")
    @Disabled
    @Test
    void refuseUser() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/{plannerId}/participants/approve/{userId}", 1L, 1L)
                        .header("Authorization", accessToken)
                        .param("status", "false"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
