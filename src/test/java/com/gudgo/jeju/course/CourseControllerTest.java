package com.gudgo.jeju.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.planner.course.dto.request.CourseUpdateRequestDto;
import com.gudgo.jeju.domain.planner.course.query.CourseQueryService;
import com.gudgo.jeju.domain.planner.course.service.CourseService;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
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
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseQueryService courseQueryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("코스 수정")
//    @Disabled
    @Test
    void updateCourse() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        CourseUpdateRequestDto updateRequestDto = new CourseUpdateRequestDto(
                "수정 제목 입니다."
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/v1/users/{userId}/planners/{plannerId}/course", 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());
    }
}
