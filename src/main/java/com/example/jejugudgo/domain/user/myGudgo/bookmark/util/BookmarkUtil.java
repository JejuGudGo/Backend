package com.example.jejugudgo.domain.user.myGudgo.bookmark.util;

import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.Bookmark;
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

    public Bookmark isBookmarked(HttpServletRequest request, BookmarkType type, Long id) {
        if (type.equals(BookmarkType.JEJU_GUDGO))
            return isJejuGudgoCourseBookmarked(request, id);

        else if (type.equals(BookmarkType.OLLE))
            return isJejuOlleCourseBookmarked(request, id);

        else if (type.equals(BookmarkType.TRAIL))
            return isJejuTrailBookmarked(request, id);

        return null;
    }

    private Bookmark isJejuGudgoCourseBookmarked(HttpServletRequest request, Long id) {
        User user = findUser(request);
        return bookMarkRepository
                .findByUserAndBookMarkTypeAndTargetId(user, BookmarkType.JEJU_GUDGO, id)
                .orElse(null);
    }

    private Bookmark isJejuOlleCourseBookmarked(HttpServletRequest request, Long id) {
        User user = findUser(request);
        return bookMarkRepository
                .findByUserAndBookMarkTypeAndTargetId(user, BookmarkType.OLLE, id)
                .orElse(null);
    }

    private Bookmark isJejuTrailBookmarked(HttpServletRequest request, Long id) {
        User user = findUser(request);
        return bookMarkRepository
                .findByUserAndBookMarkTypeAndTargetId(user, BookmarkType.TRAIL, id)
                .orElse(null);
    }

    private User findUser(HttpServletRequest request) {
        Long userId = tokenUtil.getUserIdFromHeader(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return user;
    }
}
