package com.gudgo.jeju.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.post.column.dto.request.ColumnPostCreateRequest;
import com.gudgo.jeju.domain.post.column.dto.request.ColumnPostUpdateRequest;
import com.gudgo.jeju.domain.post.column.query.ColumnPostQueryService;
import com.gudgo.jeju.domain.post.column.service.ColumnPostService;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ColumnPostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private SubjectExtractor subjectExtractor;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ColumnPostService columnPostService;

    @Autowired
    private ColumnPostQueryService columnPostQueryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    static int postCount = 1;
    static int imageCount = 1;


    @DisplayName("칼럼 - 리스트 반환")
    @Disabled
    @Test
    void getColumnPosts() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
        int page = 0;
        int size = 10;
        boolean lastPage = false;

        while (!lastPage) {
            MvcResult result = mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/api/v1/posts/columns")
                            .header("Authorization", accessToken)
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

            String responseContent = result.getResponse().getContentAsString();
            JsonNode responseJson = objectMapper.readTree(responseContent);

            lastPage = responseJson.path("last").asBoolean();

            System.out.println("Page " + page + ": " + responseJson);

            page++;
        }
    }

    @DisplayName("칼럼 - 게시글 상세")
    @Test
    @Disabled
    void getColumnPost() throws Exception {
        String accessToken = "Bearer "+ tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/columns/{postId}", 104L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @DisplayName("칼럼 - 게시글 생성")
    @RepeatedTest(100)
    @Disabled
    void createColumnPost() throws Exception{
        String accessToken = "Bearer "+ tokenGenerator.generateToken(TokenType.ACCESS, "1");
        String title = "테스트 게시글 " + postCount ++;
        String content = "칼럼 내용 입니다.";

        ColumnPostCreateRequest createDto = new ColumnPostCreateRequest(
                1L,
                title,
                content
        );

        MockMultipartFile file1 = new MockMultipartFile("image", "file" + imageCount ++ + ".jpg", "image/jpeg", content.getBytes());
        MockMultipartFile file2 = new MockMultipartFile("image", "file" + imageCount ++ + ".jpg", "image/jpeg", content.getBytes());
        MockMultipartFile file3 = new MockMultipartFile("image", "file" + imageCount ++ + ".jpg", "image/jpeg", content.getBytes());

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createDto)
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .multipart("/api/v1/posts/columns")
                        .file(request)
                        .file(file1)
                        .file(file2)
                        .file(file3)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @DisplayName("칼럼 - 접근 제한된 유저가 게시글을 작성 하려는 경우")
    @Test()
    @Disabled
    void createColumnPostByUser() throws Exception{
        String accessToken = "Bearer "+ tokenGenerator.generateToken(TokenType.ACCESS, "2");
        String title = "테스트 게시글 " + postCount ++;
        String content = "칼럼 내용 입니다." + postCount ++;

        ColumnPostCreateRequest createDto = new ColumnPostCreateRequest(
                2L,
                title,
                content
        );

        MockMultipartFile file1 = new MockMultipartFile("image", "file" + imageCount ++ + ".jpg", "image/jpeg", content.getBytes());
        MockMultipartFile file2 = new MockMultipartFile("image", "file" + imageCount ++ + ".jpg", "image/jpeg", content.getBytes());
        MockMultipartFile file3 = new MockMultipartFile("image", "file" + imageCount ++ + ".jpg", "image/jpeg", content.getBytes());

        MockMultipartFile request = new MockMultipartFile(
                "request",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createDto)
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .multipart("/api/v1/posts/columns")
                        .file(request)
                        .file(file1)
                        .file(file2)
                        .file(file3)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("칼럼 - 게시글 수정")
    @Test
    @Disabled
    void updateColumnPost() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        ColumnPostUpdateRequest request = new ColumnPostUpdateRequest(
                "변경한 제목입니다.",
                null
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/v1/posts/columns/{postId}", 103L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @DisplayName("칼럼 - 게시글 삭제")
    @Test
    @Disabled
    void deleteCoursePost() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/v1/posts/columns/{postId}", 103L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}