package com.gudgo.jeju.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.planner.courseMedia.dto.request.CourseMediaCreateRequestDto;
import com.gudgo.jeju.domain.planner.courseMedia.dto.request.CourseMediaUpdateRequestDto;
import com.gudgo.jeju.domain.planner.courseMedia.service.CourseMediaService;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseMediaControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseMediaService courseMediaService;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("모든 기록 반환")
    @Test
    @Disabled
    void getMedias() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{userId}/planners/{plannerId}/course/medias", 1L, 11L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("특정 기록 반환")
    @Test
    @Disabled
    void getMedia() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{userId}/planners/{plannerId}/course/medias/{mediaId}", 1L, 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("기록 생성")
    @Test
    @Disabled
    void createMedia() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        CourseMediaCreateRequestDto createRequestDto = new CourseMediaCreateRequestDto(
                "내용 입니다.",
                0.01,
                0.01
        );

        MockMultipartFile requestFile = new MockMultipartFile(
                "request",
                "",
                "application/json",
                objectMapper.writeValueAsString(createRequestDto).getBytes()
        );

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                "테스트 이미지".getBytes()
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .multipart("/api/v1/users/{userId}/planners/{plannerId}/course/medias", 1L, 11L)
                        .file(requestFile)
                        .file(imageFile)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @DisplayName("기록 수정")
    @Test
    @Disabled
    void updateMedia() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        CourseMediaUpdateRequestDto createRequestDto = new CourseMediaUpdateRequestDto(
                "수정 다시 했습니당."
        );

        MockMultipartFile requestFile = new MockMultipartFile(
                "request",
                "",
                "application/json",
                objectMapper.writeValueAsString(createRequestDto).getBytes()
        );

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                "테스트 이미지".getBytes()
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .multipart("/api/v1/users/{userId}/planners/{plannerId}/course/medias/{mediaId}", 1L, 11L, 1L)
                        .file(requestFile)
                        .file(imageFile)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @DisplayName("기록 삭제")
    @Test
    @Disabled
    void deleteMedia() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/{userId}/planners/{plannerId}/course/medias/{mediaId}", 1L, 1L, 1L)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());
    }
}
