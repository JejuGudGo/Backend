package com.example.jejugudgo.domain.user.service;

import com.example.jejugudgo.domain.user.dto.request.UserCheckListCreateRequest;
import com.example.jejugudgo.domain.user.dto.request.UserCheckListUpdateRequest;
import com.example.jejugudgo.domain.user.dto.response.UserCheckListResponse;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.entity.UserCheckList;
import com.example.jejugudgo.domain.user.repository.UserCheckListRepository;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCheckListService {
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final UserCheckListRepository userCheckListRepository;

    public List<UserCheckListResponse> getAll(HttpServletRequest request) {
        Long userId = tokenUtil.getUserIdFromHeader(request);
        List<UserCheckList> userCheckListResponses = userCheckListRepository.findAllByUserId(userId);
        return userCheckListResponses.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserCheckListResponse get(Long checkItemId) {
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));;
        return toResponse(checkList);
    }

    @Transactional
    public UserCheckListResponse create(UserCheckListCreateRequest createRequest, HttpServletRequest servletRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(servletRequest);
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(RetCode.RET_CODE97));;

        Optional<UserCheckList> latestCheckList = userCheckListRepository.findTopByUserIdOrderByOrderNumberDesc(userId);
        Long maxOrderNumber = latestCheckList.map(UserCheckList::getOrderNumber).orElse(0L);

        UserCheckList checkList = UserCheckList.builder()
                .user(user)
                .content(createRequest.content())
                .isFinished(false)
                .orderNumber(maxOrderNumber + 1)
                .build();

        userCheckListRepository.save(checkList);

        return toResponse(checkList);
    }

    // 모든 필드를 하나의 메서드에서 처리
    @Transactional
    public UserCheckListResponse updateCheckList(Long checkItemId, UserCheckListUpdateRequest request) {
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));;

        if (request.content() != null && !request.content().isEmpty()) {
            checkList = checkList.updateContent(request.content());
        }

        if (request.isFinished() != null) {
            checkList = checkList.updateIsFinished(request.isFinished());
        }

        if (request.orderNumber() != null) {
            checkList = checkList.updateOrderNumber(request.orderNumber());
        }

        userCheckListRepository.save(checkList);

        return toResponse(checkList);
    }

    // UserCheckList 객체를 UserCheckListResponse 로 변환하는 메서드
    private UserCheckListResponse toResponse(UserCheckList checkList) {
        return new UserCheckListResponse(
                checkList.getId(),
                checkList.getUser().getId(),
                checkList.getOrderNumber(),
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
