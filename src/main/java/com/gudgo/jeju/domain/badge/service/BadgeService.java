package com.gudgo.jeju.domain.badge.service;

import com.gudgo.jeju.domain.badge.dto.response.BadgeResponseDto;
import com.gudgo.jeju.domain.badge.entity.Badge;
import com.gudgo.jeju.domain.badge.entity.BadgeCode;
import com.gudgo.jeju.domain.badge.event.BadgeEvent;
import com.gudgo.jeju.domain.badge.repository.BadgeRepository;
import com.gudgo.jeju.domain.profile.service.ProfileService;
import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BadgeService {
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final ProfileService profileService;

    // 뱃지 부여 이벤트
    @EventListener
    @Transactional
    public void handleBadgeEvent(BadgeEvent event) {
        Long userId = event.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);

        Badge badge = Badge.builder()
                .user(user)
                .code(event.getCode())
                .build();
        badgeRepository.save(badge);

        log.info("====================================================================================");
        log.info("plannerEvent");
        log.info("====================================================================================");

        profileService.incrementBadgeCount(user.getProfile().getId());
    }


    public List<BadgeResponseDto> get(Long userId) {
        List<Badge> badges = badgeRepository.findByUserId(userId);

        List<BadgeResponseDto> result = new ArrayList<>();
        for (Badge badge : badges) {
            result.add(convertToDto(badge));
        }
        return result;
    }

    private BadgeResponseDto convertToDto(Badge badge) {
        UserInfoResponseDto userInfoDto = new UserInfoResponseDto(
                badge.getUser().getId(),
                badge.getUser().getEmail(),
                badge.getUser().getNickname(),
                badge.getUser().getName(),
                badge.getUser().getNumberTag(),
                badge.getUser().getProfile().getProfileImageUrl(),
                badge.getUser().getRole()
        );

        return new BadgeResponseDto(
                badge.getId(),
                userInfoDto,
                badge.getCode()
        );
    }
//
//    private User getUser(HttpServletRequest request) {
//        String token = tokenExtractor.getAccessTokenFromHeader(request);    // 요청 헤더에서 AccessToken 추출
//        Long userid = subjectExtractor.getUserIdFromToken(token);           // 토큰에서 userid 추출
//
//        return userRepository.findById(userid)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userid));
//    }
}

