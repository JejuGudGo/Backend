package com.gudgo.jeju.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.global.auth.sms.dto.SMSMessageDTO;
import com.gudgo.jeju.global.auth.sms.dto.SMSVerificationDTO;
import com.gudgo.jeju.global.auth.sms.service.SMSMessageService;
import org.junit.jupiter.api.Disabled;
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
public class SMSMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SMSMessageService smsMessageService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("인증 메세지 전송")
    @Test
    @Disabled
    void sendMessage() throws Exception {
        SMSMessageDTO request = new SMSMessageDTO(
                "김시은",
                "01012345678"
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/sms/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @DisplayName("인증 메세지 확인")
    @Test
    @Disabled
    void validateMessage() throws Exception {
        SMSVerificationDTO request = new SMSVerificationDTO(
                "김시은",
                "01065535378",
                "113749"
        );

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/sms/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
