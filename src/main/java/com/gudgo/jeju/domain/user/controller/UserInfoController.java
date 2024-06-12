package com.gudgo.jeju.domain.user.controller;


import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;
import com.gudgo.jeju.domain.user.service.UserInfoService;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@RestController
public class UserInfoController {
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;
    private final UserInfoService userInfoService;


    //    /*유저 정보 반환 */
    @GetMapping(value = "/user")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(HttpServletRequest request) {
        String token = tokenExtractor.getAccessTokenFromHeader(request);
        Long userid = subjectExtractor.getUserIdFromToken(token);

        UserInfoResponseDto userInfoResponse = userInfoService.get(userid);

        return ResponseEntity.ok(userInfoResponse);


    }


}
