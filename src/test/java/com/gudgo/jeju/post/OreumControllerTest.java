package com.gudgo.jeju.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.oreum.query.OreumSpotQueryService;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OreumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private OreumSpotQueryService oreumSpotQueryService;

    @DisplayName("오름 스팟 리스트 반환")
    @Test
    @Disabled
    void getOreumSpots() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
        Pageable pageable = PageRequest.of(0, 10);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/oreum")
                        .header("Authorization", accessToken)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }

    @DisplayName("오름 상세 조회")
    @Test
//    @Disabled
    void getOreumDetail() throws Exception {
        String accessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, "1");
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/oreum/{oreumId}", 181L)
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);
    }
}
