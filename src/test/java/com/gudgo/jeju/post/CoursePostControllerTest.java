//package com.gudgo.jeju.post;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.gudgo.jeju.domain.post.walk.dto.request.CoursePostCreateRequest;
//import com.gudgo.jeju.domain.post.walk.dto.request.CoursePostUpdateRequest;
//import com.gudgo.jeju.domain.post.walk.service.CoursePostService;
//import com.gudgo.jeju.domain.post.walk.query.CoursePostQueryService;
//import com.gudgo.jeju.global.jwt.token.TokenGenerator;
//import com.gudgo.jeju.global.jwt.token.TokenType;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class CoursePostControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private TokenGenerator tokenGenerator;
//
//    @Autowired
//    private CoursePostQueryService coursePostQueryService;
//
//    @Autowired
//    private CoursePostService coursePostService;
//
//    @DisplayName("코스 게시물 - 리스트 반환")
//    @Test
//    @Disabled
//    void getCoursePosts() throws Exception {
//        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
//        Pageable pageable = PageRequest.of(0, 10);
//
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .get("/api/v1/posts")
//                        .header("Authorization", accessToken)
//                        .param("page", String.valueOf(pageable.getPageNumber()))
//                        .param("size", String.valueOf(pageable.getPageSize()))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseContent = mvcResult.getResponse().getContentAsString();
//        System.out.println("Response Content: " + responseContent);
//    }
//
//    @DisplayName("코스 게시물 - 생성 (사용자)")
//    @Test
//    @Disabled
//    void createCoursePostByUsers() throws Exception {
//        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
//
//        CoursePostCreateRequest request = new CoursePostCreateRequest(
//                1L,
//                "이거 같이 걸으실 분 괌",
//                5L,
//                "안녕하시와요. 얼른 신청하시와요.",
//                1L,
//                null,
//                "어쩌구 저쩌구",
//                0.01,
//                0.01
//        );
//
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/posts/planners")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseContent = mvcResult.getResponse().getContentAsString();
//        System.out.println("Response Content: " + responseContent);
//    }
//
//    @DisplayName("코스 게시물 - 생성 (올레)")
//    @Test
//    @Disabled
//    void createCoursePostByOlle() throws Exception {
//        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
//
//        CoursePostCreateRequest request = new CoursePostCreateRequest(
//                1L,
//                "올레 같이 걸으실 분 괌",
//                5L,
//                "안녕하시와요. 얼른 신청하시와요.",
//                null,
//                1L,
//                "어쩌구 저쩌구",
//                0.01,
//                0.01
//        );
//
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/posts/olle")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseContent = mvcResult.getResponse().getContentAsString();
//        System.out.println("Response Content: " + responseContent);
//    }
//
//    @DisplayName("코스 게시물 - 수정")
//    @Test
//    @Disabled
//    void updateCoursePost() throws Exception {
//        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
//
//        CoursePostUpdateRequest request = new CoursePostUpdateRequest(
//                "제목 수정함",
//                null,
//                11L,
//                null,
//                false
//        );
//
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .patch("/api/v1/posts/{postId}", 1L)
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseContent = mvcResult.getResponse().getContentAsString();
//        System.out.println("Response Content: " + responseContent);
//    }
//
//    @DisplayName("코스 게시물 - 삭제")
//    @Test
//    @Disabled
//    void deleteCoursePost() throws Exception {
//        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
//
//        mockMvc
//                .perform(MockMvcRequestBuilders
//                        .delete("/api/v1/posts/{postId}", 2L)
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//    }
//}