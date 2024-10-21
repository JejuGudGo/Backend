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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCheckListService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserCheckListRepository userCheckListRepository;

    public List<UserCheckListResponse> getAll(HttpServletRequest request) {
        // 1. userId 가져오기
        Long userId = userService.getAuthenticatedUserIdFromToken(request);

        // 2. 해당 사용자의 체크리스트 항목 조회
        List<UserCheckList> userCheckListResponses
                = userCheckListRepository.findAllByUserId(userId);

        // 3. UserCheckListResponse 로 변환하여 반환
        return userCheckListResponses.stream()
                .map(checkList -> new UserCheckListResponse(
                        checkList.getId(),
                        checkList.getUser().getId(),
                        checkList.getContent(),
                        checkList.isFinished()
                )).collect(Collectors.toList());
    }

    public UserCheckListResponse get(Long checkItemId) {
        // 1. id 값으로 체크리스트 항목 조회
        UserCheckList userCheckList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(EntityNotFoundException::new);

        // 2. UserCheckListResponse 로 변환하여 반환
        return new UserCheckListResponse(
                checkItemId,
                userCheckList.getUser().getId(),
                userCheckList.getContent(),
                userCheckList.isFinished()
        );
    }

    public UserCheckListResponse create(UserCheckListCreateRequest createRequest, HttpServletRequest servletRequest) {
        // 1. 사용자 조회
        Long userId = userService.getAuthenticatedUserIdFromToken(servletRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);


        // 2.
        UserCheckList checkList = UserCheckList.builder()
                .user(user)
                .content(createRequest.content())
                .isFinished(false)
                .build();

        userCheckListRepository.save(checkList);

        return new UserCheckListResponse(
                checkList.getId(),
                checkList.getUser().getId(),
                checkList.getContent(),
                checkList.isFinished()
        );
    }

    public void updateContent(Long checkItemId, UserCheckListUpdateRequest request) {
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(EntityNotFoundException::new);

        if (request.content() != null) {
            checkList = checkList.updateContent(request.content());
        }

        userCheckListRepository.save(checkList);

        new UserCheckListResponse(
                checkList.getId(),
                checkList.getUser().getId(),
                checkList.getContent(),
                checkList.isFinished()
        );
    }

    public UserCheckListResponse updateFinish(Long checkItemId) {
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(EntityNotFoundException::new);

        checkList = checkList.updateIsFinished(true);

        userCheckListRepository.save(checkList);

        return new UserCheckListResponse(
                checkList.getId(),
                checkList.getUser().getId(),
                checkList.getContent(),
                checkList.isFinished()
        );
    }


}
