//package com.gudgo.jeju.review;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.gudgo.jeju.global.jwt.token.TokenGenerator;
//import com.gudgo.jeju.global.jwt.token.TokenType;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserReviewControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private TokenGenerator tokenGenerator;
//
//    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//
//    @DisplayName("리뷰 - 특정 유저 리뷰 목록 조회")
//    @Test
//    @Disabled
//    void getReviewList() throws Exception {
//        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
//        Pageable pageable = PageRequest.of(0, 10);
//
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .get("/api/v1/users/{userId}/reviews", 2L)
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
//    @DisplayName("리뷰 - 특정 플래너 리뷰 생성")
//    @RepeatedTest(10)
////    @Disabled
//    void create() throws Exception {
//        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
//
//        List<ReviewCategoryRequestDto> reviewCategories = new ArrayList<>();
//        List<ReviewTagRequestDto> reviewTagRequestDtoList = new ArrayList<>();
//
//        ReviewTagRequestDto reviewTagRequestDto1 = new ReviewTagRequestDto("RT01");
//        ReviewTagRequestDto reviewTagRequestDto2 = new ReviewTagRequestDto("RT02");
//        reviewTagRequestDtoList.add(reviewTagRequestDto1);
//        reviewTagRequestDtoList.add(reviewTagRequestDto2);
//
//        ReviewCategoryRequestDto reviewCategoryRequestDto1 = new ReviewCategoryRequestDto(
//                "R01", reviewTagRequestDtoList
//        );
//        reviewCategories.add(reviewCategoryRequestDto1);
//        reviewTagRequestDtoList.clear();
//
//        ReviewTagRequestDto reviewTagRequestDto3 = new ReviewTagRequestDto("RT08");
//        ReviewTagRequestDto reviewTagRequestDto4 = new ReviewTagRequestDto("RT09");
//        reviewTagRequestDtoList.add(reviewTagRequestDto3);
//        reviewTagRequestDtoList.add(reviewTagRequestDto4);
//
//        ReviewCategoryRequestDto reviewCategoryRequestDto2 = new ReviewCategoryRequestDto(
//                "R02", reviewTagRequestDtoList
//        );
//        reviewCategories.add(reviewCategoryRequestDto2);
//        reviewTagRequestDtoList.clear();
//
//        ReviewTagRequestDto reviewTagRequestDto5 = new ReviewTagRequestDto("RT18");
//        ReviewTagRequestDto reviewTagRequestDto6 = new ReviewTagRequestDto("RT19");
//        reviewTagRequestDtoList.add(reviewTagRequestDto5);
//        reviewTagRequestDtoList.add(reviewTagRequestDto6);
//
//        ReviewCategoryRequestDto reviewCategoryRequestDto3 = new ReviewCategoryRequestDto(
//                "R03", reviewTagRequestDtoList
//        );
//        reviewCategories.add(reviewCategoryRequestDto3);
//        reviewTagRequestDtoList.clear();
//
//        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(
//                "리뷰 입니다.",
//                LocalDate.now(),
//                reviewCategories,
//                1L
//        );
//
//        String reviewJson = objectMapper.writeValueAsString(reviewRequestDto);
//
//        MockMultipartFile image1 = new MockMultipartFile("image", "image1.jpg", "image/jpeg", "image1 content".getBytes());
//        MockMultipartFile image2 = new MockMultipartFile("image", "image2.jpg", "image/jpeg", "image2 content".getBytes());
//        MockMultipartFile jsonPart = new MockMultipartFile("request", "", "application/json", reviewJson.getBytes());
//
//        mockMvc
//                .perform(MockMvcRequestBuilders
//                        .multipart("/api/v1/users/{userId}/planners/{plannerId}/reviews", 1L, 2L)
//                        .file(image1)
//                        .file(image2)
//                        .file(jsonPart)
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())
//                .andReturn();
//    }
//
//    @DisplayName("리뷰 - 수정")
//    @Test
//    @Disabled
//    void update() throws Exception {
//        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
//
//        List<ReviewCategoryRequestDto> reviewCategories = new ArrayList<>();
//        List<ReviewTagRequestDto> reviewTagRequestDtoList = new ArrayList<>();
//
//        ReviewTagRequestDto reviewTagRequestDto1 = new ReviewTagRequestDto("RT01");
//        ReviewTagRequestDto reviewTagRequestDto2 = new ReviewTagRequestDto("RT02");
//        reviewTagRequestDtoList.add(reviewTagRequestDto1);
//        reviewTagRequestDtoList.add(reviewTagRequestDto2);
//
//        ReviewCategoryRequestDto reviewCategoryRequestDto1 = new ReviewCategoryRequestDto(
//                "R01", reviewTagRequestDtoList
//        );
//        reviewCategories.add(reviewCategoryRequestDto1);
//        reviewTagRequestDtoList.clear();
//
//        ReviewTagRequestDto reviewTagRequestDto3 = new ReviewTagRequestDto("RT08");
//        ReviewTagRequestDto reviewTagRequestDto4 = new ReviewTagRequestDto("RT09");
//        reviewTagRequestDtoList.add(reviewTagRequestDto3);
//        reviewTagRequestDtoList.add(reviewTagRequestDto4);
//
//        ReviewCategoryRequestDto reviewCategoryRequestDto2 = new ReviewCategoryRequestDto(
//                "R02", reviewTagRequestDtoList
//        );
//        reviewCategories.add(reviewCategoryRequestDto2);
//        reviewTagRequestDtoList.clear();
//
//        ReviewTagRequestDto reviewTagRequestDto5 = new ReviewTagRequestDto("RT18");
//        ReviewTagRequestDto reviewTagRequestDto6 = new ReviewTagRequestDto("RT19");
//        reviewTagRequestDtoList.add(reviewTagRequestDto5);
//        reviewTagRequestDtoList.add(reviewTagRequestDto6);
//
//        ReviewCategoryRequestDto reviewCategoryRequestDto3 = new ReviewCategoryRequestDto(
//                "R02", reviewTagRequestDtoList
//        );
//        reviewCategories.add(reviewCategoryRequestDto3);
//        reviewTagRequestDtoList.clear();
//
//        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(
//                "리뷰 입니다.",
//                LocalDate.now(),
//                reviewCategories,
//                1L
//        );
//
//        String reviewJson = objectMapper.writeValueAsString(reviewRequestDto);
//
//        MockMultipartFile image1 = new MockMultipartFile("image", "image1.jpg", "image/jpeg", "image1 content".getBytes());
//        MockMultipartFile image2 = new MockMultipartFile("image", "image2.jpg", "image/jpeg", "image2 content".getBytes());
//        MockMultipartFile jsonPart = new MockMultipartFile("request", "", "application/json", reviewJson.getBytes());
//
//        mockMvc
//                .perform(MockMvcRequestBuilders
//                        .multipart("/api/v1/users/{userId}/planners/{plannerId}/reviews", 1L, 2L)
//                        .file(image1)
//                        .file(image2)
//                        .file(jsonPart)
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())
//                .andReturn();
//    }
//
//    @DisplayName("리뷰 - 삭제")
//    @Test
//    @Disabled
//    void delete() throws Exception {
//        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
//
//        mockMvc
//                .perform(MockMvcRequestBuilders
//                        .delete("/api/v1/users/{userId}/reviews/{reviewId}", 2L, 1L)
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//    }
//}
