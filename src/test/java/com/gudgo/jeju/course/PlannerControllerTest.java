package com.gudgo.jeju.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.planner.dto.request.course.PlannerCreateRequestDto;
import com.gudgo.jeju.domain.planner.dto.request.course.PlannerUpdateRequestDto;
import com.gudgo.jeju.domain.planner.query.PlannerQueryService;
import com.gudgo.jeju.domain.planner.service.PlannerService;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlannerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlannerQueryService plannerQueryService;

    @Autowired
    private PlannerService plannerService;


    static int plannerCnt = 1;

    @DisplayName("플래너 - 유저 플래너 반환")
    @Disabled
    @Test
    void getUserPlanners() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/planners/course/user", 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("플래너 - 특정 유저 플래너 반환")
    @Disabled
    @Test
    void getMyPlanners() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{userId}/planners", 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("status", "all")
                        .param("status", "false")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("플래너 - 생성")
    @Disabled
    @RepeatedTest(10)
//    @Test
    void createPlanner() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
        String title = "테스트 플래너 " + plannerCnt++;

        PlannerCreateRequestDto requestDto = new PlannerCreateRequestDto(
                title,
                LocalDate.now(),
                1L,
                null,
                false
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/users/{userId}/planners", 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @DisplayName("플래너 - 수정")
    @Disabled
    @Test
    void update() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        PlannerUpdateRequestDto requestDto = new PlannerUpdateRequestDto(
                null,
                true,
                null,
                null,
                true
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/v1/users/{userId}/planners/{plannerId}", 1L, 141)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @DisplayName("플래너 - 삭제")
    @Disabled
    @Test
    void delete() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/{userId}/planners/{plannerId}", 1L, 141)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
