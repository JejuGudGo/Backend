package com.example.jejugudgo.domain.user.checklist.service;

import com.example.jejugudgo.domain.user.checklist.dto.request.UserCheckListCreateRequest;
import com.example.jejugudgo.domain.user.checklist.dto.request.UserCheckListUpdateRequest;
import com.example.jejugudgo.domain.user.checklist.dto.response.UserCheckListResponse;
import com.example.jejugudgo.domain.user.checklist.entity.UserCheckList;
import com.example.jejugudgo.domain.user.checklist.enums.DefaultCheckList;
import com.example.jejugudgo.domain.user.checklist.event.UserCheckListEvent;
import com.example.jejugudgo.domain.user.checklist.repository.UserCheckListRepository;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UserCheckListService {
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final UserCheckListRepository userCheckListRepository;

    @EventListener
    @Transactional
    public void handleTodoEvent(UserCheckListEvent event) {
        Long userId = event.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));;

        List<UserCheckList> checkLists = IntStream.range(0, DefaultCheckList.getDefaultCheckList().size())
                .mapToObj(index -> {
                    DefaultCheckList checklistItem = DefaultCheckList.getDefaultCheckList().get(index);

                    return UserCheckList.builder()
                            .user(user)
                            .content(checklistItem.getCheckItem()) // 열거형에서 checkItem 가져오기
                            .isFinished(false)
                            .orders((long) index) // order 가 0부터 시작하도록 설정
                            .build();
                })
                .collect(Collectors.toList());

        userCheckListRepository.saveAll(checkLists);
    }


    public List<UserCheckListResponse> getAll(HttpServletRequest httpRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(httpRequest);
        List<UserCheckList> userCheckListResponses = userCheckListRepository.findAllByUserId(userId);

        return userCheckListResponses.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserCheckListResponse get(Long checkItemId) {
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return toResponse(checkList);
    }

    @Transactional
    public UserCheckListResponse create(UserCheckListCreateRequest request, HttpServletRequest httpRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(httpRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        Optional<UserCheckList> latestCheckList = userCheckListRepository.findTopByUserIdOrderByOrdersDesc(userId);
        Long maxOrderNumber = latestCheckList.map(UserCheckList::getOrders).orElse(0L);

        UserCheckList checkList = UserCheckList.builder()
                .user(user)
                .content(request.content())
                .isFinished(false)
                .orders(maxOrderNumber + 1)
                .build();

        userCheckListRepository.save(checkList);

        return toResponse(checkList);
    }

    // 모든 필드를 하나의 메서드에서 처리
    @Transactional
    public UserCheckListResponse updateCheckList(Long checkItemId, UserCheckListUpdateRequest request) {
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));;

        if (request.content() != null && !request.content().isEmpty())
            checkList = checkList.updateContent(request.content());

        if (request.isFinished() != null)
            checkList = checkList.updateIsFinished(request.isFinished());

        if (request.order() != null)
            checkList = checkList.updateOrder(request.order());

        userCheckListRepository.save(checkList);

        return toResponse(checkList);
    }

    // UserCheckList 객체를 UserCheckListResponse 로 변환하는 메서드
    private UserCheckListResponse toResponse(UserCheckList checkList) {
        return new UserCheckListResponse(
                checkList.getId(),
                checkList.getUser().getId(),
                checkList.getOrders(),
                checkList.getContent(),
                checkList.isFinished()
        );
    }

    public void delete(Long checkItemId) {
        // 1. 체크리스트 항목 조회
        UserCheckList userCheckList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));;

        // 2. 체크리스트 항목 삭제
        userCheckListRepository.delete(userCheckList);
    }
}
