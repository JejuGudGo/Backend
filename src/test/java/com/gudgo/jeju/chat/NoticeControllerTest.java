package com.gudgo.jeju.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.post.chat.dto.request.NoticeCreateRequest;
import com.gudgo.jeju.domain.post.chat.dto.request.NoticeUpdateRequest;
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
public class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGenerator tokenGenerator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("공지사항 조회")
    @Test
    @Disabled
    void getNotices() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{userId}/chatRooms/{chatRoomId}/notices", 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("최신 공지사항 조회")
    @Test
    @Disabled
    void getLatestNotice() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{userId}/chatRooms/{chatRoomId}/notices/latest", 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    @DisplayName("공지사항 생성")
    @Test
    @Disabled
    void createNotice() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        NoticeCreateRequest request = new NoticeCreateRequest(
                "새로운 공지사항"
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/{userId}/chatRooms/{chatRoomId}/notices", 1L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @DisplayName("공지사항 수정")
    @Test
    @Disabled
    void updateNotice() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        NoticeUpdateRequest request = new NoticeUpdateRequest(
                1L,
                "수정된 공지사항"
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/users/{userId}/chatRooms/{chatRoomId}/notices/{noticeId}", 1L, 1L, 2L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @DisplayName("공지사항 삭제")
    @Test
//    @Disabled
    void deleteNotice() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/{userId}/chatRooms/{chatRoomId}/notices/{noticeId}", 1L, 1L, 2L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
