package com.example.jejugudgo.domain.user.service;

import com.example.jejugudgo.domain.user.dto.request.UserCheckListCreateRequest;
import com.example.jejugudgo.domain.user.dto.request.UserCheckListUpdateRequest;
import com.example.jejugudgo.domain.user.dto.response.UserCheckListResponse;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.entity.UserCheckList;
import com.example.jejugudgo.domain.user.repository.UserCheckListRepository;
import com.example.jejugudgo.domain.user.repository.UserRepository;
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
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserCheckListRepository userCheckListRepository;

    public List<UserCheckListResponse> getAll(HttpServletRequest request) {
        Long userId = userService.getAuthenticatedUserIdFromToken(request);
        List<UserCheckList> userCheckListResponses = userCheckListRepository.findAllByUserId(userId);
        return userCheckListResponses.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserCheckListResponse get(Long checkItemId) {
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(EntityNotFoundException::new);
        return toResponse(checkList);
    }

    @Transactional
    public UserCheckListResponse create(UserCheckListCreateRequest createRequest, HttpServletRequest servletRequest) {
        Long userId = userService.getAuthenticatedUserIdFromToken(servletRequest);
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

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
                .orElseThrow(EntityNotFoundException::new);

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
}