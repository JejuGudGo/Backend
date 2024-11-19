package com.example.jejugudgo.domain.user.myGudgo.bookmark.util;

import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.repository.BookmarkRepository;
import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.domain.user.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkUtil {
    private final UserRepository userRepository;
    private final BookmarkRepository bookMarkRepository;
    private final TokenUtil tokenUtil;

    public boolean isBookmarked(HttpServletRequest request, BookmarkType type, Long id) {
        if (type.equals(BookmarkType.JEJU_GUDGO))
            return isJejuGudgoCourseBookmarked(request, id);

        else if (type.equals(BookmarkType.OLLE))
            return isJejuOlleCourseBookmarked(request, id);

        else if (type.equals(BookmarkType.TRAIL))
            return isJejuTrailBookmarked(request, id);

        return false;
    }

    private boolean isJejuGudgoCourseBookmarked(HttpServletRequest request, Long id) {
        User user = findUser(request);
        return bookMarkRepository.existsByUserAndBookMarkTypeAndTargetId(user, BookmarkType.JEJU_GUDGO, id);
    }

    private boolean isJejuOlleCourseBookmarked(HttpServletRequest request, Long id) {
        User user = findUser(request);
        return bookMarkRepository.existsByUserAndBookMarkTypeAndTargetId(user, BookmarkType.OLLE, id);
    }

    private boolean isJejuTrailBookmarked(HttpServletRequest request, Long id) {
        User user = findUser(request);
        return bookMarkRepository.existsByUserAndBookMarkTypeAndTargetId(user, BookmarkType.TRAIL, id);
    }

    private User findUser(HttpServletRequest request) {
        Long userId = tokenUtil.getUserIdFromHeader(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return user;
    }
}
