package com.gudgo.jeju.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.post.dto.request.CommentCreateRequest;
import com.gudgo.jeju.domain.post.dto.request.CommentUpdateRequest;
import com.gudgo.jeju.domain.post.query.CommentQueryService;
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
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private CommentQueryService commentQueryService;

    @Autowired
    private CommentService commentService;

    @DisplayName("댓글 - 리스트 반환")
    @Test
    @Disabled
    void getComments() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/{postId}/comments", 105L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("댓글 - 생성")
    @RepeatedTest(100)
    @Disabled
    void createComment() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        CommentCreateRequest request = new CommentCreateRequest(1L, "댓글 내용입니다.");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/posts/{postId}/comments", 105L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("댓글 - 수정")
    @Test
    @Disabled
    void updateComment() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        CommentUpdateRequest request = new CommentUpdateRequest("수정된 댓글 내용입니다.");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/v1/posts/{postId}/comments/{commentId}", 105L, 1L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("댓글 - 삭제")
    @Test
    @Disabled
    void deleteComment() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/v1/posts/{postId}/comments/{commentId}", 105L, 2L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("대댓글 - 리스트 반환")
    @Test
    @Disabled
    void getNestedComments() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/{postId}/comments/{commentId}/nested", 105L, 3L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("대댓글 - 생성")
    @RepeatedTest(100)
    @Disabled
    void createNestedComment() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        CommentCreateRequest request = new CommentCreateRequest(1L,"대댓글 내용입니다.");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/posts/{postId}/comments/{commentId}/nested", 105L, 3L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("대댓글 - 수정")
    @Test
    @Disabled
    void updateNestedComment() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        CommentUpdateRequest request = new CommentUpdateRequest("수정된 대댓글 내용입니다.");

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/v1/posts/{postId}/comments/{commentId}/nested/{nestedId}", 105L, 3L, 105L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("대댓글 - 삭제")
    @Test
    @Disabled
    void deleteNestedComment() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/v1/posts/{postId}/comments/{commentId}/nested/{nestedId}", 105L, 3L, 102L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}
