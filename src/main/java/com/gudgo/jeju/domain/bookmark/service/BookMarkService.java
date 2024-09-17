package com.gudgo.jeju.domain.bookmark.service;

import com.gudgo.jeju.domain.bookmark.dto.request.BookmarkCreateRequestDto;
import com.gudgo.jeju.domain.bookmark.dto.response.BookMarkResponseDto;
import com.gudgo.jeju.domain.bookmark.entity.BookMark;
import com.gudgo.jeju.domain.bookmark.repository.BookMarkRepository;
import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerTagResponse;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookMarkService {
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;

    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;
    private final BookMarkRepository bookMarkRepository;


    @Transactional
    public void create(HttpServletRequest request, BookmarkCreateRequestDto requestDto) {
        Long plannerId = requestDto.plannerId();
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        User user = getUser(request);

        BookMark bookMark = BookMark.builder()
                .user(user)
                .planner(planner)
                .build();

        bookMarkRepository.save(bookMark);
    }

    private User getUser(HttpServletRequest request) {
        String token = tokenExtractor.getAccessTokenFromHeader(request);    // 요청 헤더에서 AccessToken 추출
        Long userid = subjectExtractor.getUserIdFromToken(token);           // 토큰에서 userid 추출

        return userRepository.findById(userid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userid));
    }

    public List<BookMarkResponseDto> get(HttpServletRequest request) {
        Long userId = getUser(request).getId();
        List<BookMark> bookMarks = bookMarkRepository.findByUserIdAndIsDeletedFalse(userId);

        List<BookMarkResponseDto> result = new ArrayList<>();
        for (BookMark bookMark : bookMarks) {
            result.add(convertToDto(bookMark));
        }

        return result;
    }

    private BookMarkResponseDto convertToDto(BookMark bookMark) {
        UserInfoResponseDto userInfoDto = new UserInfoResponseDto(
                bookMark.getUser().getId(),
                bookMark.getUser().getEmail(),
                bookMark.getUser().getNickname(),
                bookMark.getUser().getName(),
                bookMark.getUser().getNumberTag(),
                bookMark.getUser().getProfile().getProfileImageUrl(),
                bookMark.getUser().getRole()
        );

        return new BookMarkResponseDto(
                bookMark.getId(),
                userInfoDto,
                bookMark.getPlanner().getId()
        );
    }

    public void delete(Long bookMarkId) {
        BookMark bookMark = bookMarkRepository.findById(bookMarkId)
                .orElseThrow(EntityNotFoundException::new);

        bookMark = bookMark.withDeleted(bookMarkId, true);

        bookMarkRepository.save(bookMark);
    }
}
