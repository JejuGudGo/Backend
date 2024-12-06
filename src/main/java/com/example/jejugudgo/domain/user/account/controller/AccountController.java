package com.example.jejugudgo.domain.user.account.controller;

import com.example.jejugudgo.domain.user.account.dto.request.FindEmailRequest;
import com.example.jejugudgo.domain.user.account.dto.request.PasswordUpdateRequest;
import com.example.jejugudgo.domain.user.account.dto.request.SignUpRequest;
import com.example.jejugudgo.domain.user.account.dto.response.FindEmailResponse;
import com.example.jejugudgo.domain.user.account.dto.response.SignUpResponse;
import com.example.jejugudgo.domain.user.account.service.AccountService;
import com.example.jejugudgo.domain.user.account.service.SignUpService;
import com.example.jejugudgo.domain.user.account.service.UserProfileService;
import com.example.jejugudgo.domain.user.common.validation.ValidationManager;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class AccountController {
    private final ApiResponseUtil apiResponseUtil;
    private final AccountService accountService;
    private final SignUpService signUpService;
    private final UserProfileService userProfileService;
    private final ValidationManager validationManager;

    @GetMapping(value =  "/signup/check")
    public ResponseEntity<CommonApiResponse> checkEmailDuplicate(@RequestParam("email") String email ) {
        validationManager.validateEmailDuplication(email);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<CommonApiResponse> signup(@RequestBody SignUpRequest request) {
        SignUpResponse response = signUpService.signUp(request);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    @PostMapping("/find/email")
    ResponseEntity<CommonApiResponse> findUserByEmail(@RequestBody FindEmailRequest request) {
        List<FindEmailResponse> responses = accountService.findEmail(request);
        return ResponseEntity.ok(apiResponseUtil.success(responses, "results"));
    }

    @PostMapping("/find/pass")
    public ResponseEntity<CommonApiResponse> findUserAndResetPassword(@RequestBody PasswordUpdateRequest request) {
        userProfileService.updatePassword(request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }

    @PostMapping("/user/cancel")
    public ResponseEntity<CommonApiResponse> cancelUser(HttpServletRequest httpRequest) {
        accountService.cancelAccount(httpRequest);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}
