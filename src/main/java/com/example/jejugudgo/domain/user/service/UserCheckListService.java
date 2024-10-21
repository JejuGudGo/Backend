package com.example.jejugudgo.domain.user.service;

import com.example.jejugudgo.domain.user.dto.request.UserCheckListContentUpdateRequest;
import com.example.jejugudgo.domain.user.dto.request.UserCheckListCreateRequest;
import com.example.jejugudgo.domain.user.dto.request.UserCheckListFinishRequest;
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
        // 1. 사용자 id 가져오기
        Long userId = userService.getAuthenticatedUserIdFromToken(request);

        // 2. 해당 사용자의 체크리스트 항목 조회
        List<UserCheckList> userCheckListResponses
                = userCheckListRepository.findAllByUserId(userId);

        // 3. UserCheckListResponse 로 변환하여 반환
        return userCheckListResponses.stream()
                .map(checkList -> toResponse(checkList))
                .collect(Collectors.toList());
    }

    public UserCheckListResponse get(Long checkItemId) {
        // 1. id 값으로 체크리스트 항목 조회
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(EntityNotFoundException::new);

        // 2. UserCheckListResponse 로 변환하여 반환
        return toResponse(checkList);
    }

    public UserCheckListResponse create(UserCheckListCreateRequest createRequest, HttpServletRequest servletRequest) {
        // 1. 사용자 id 추출 후 사용자 엔티티 조회
        Long userId = userService.getAuthenticatedUserIdFromToken(servletRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);


        // 2. 새로운 체크리스트 항목 생성
        UserCheckList checkList = UserCheckList.builder()
                .user(user)
                .content(createRequest.content())
                .isFinished(false)
                .build();

        // 3. 생성된 체크리스트 저장
        userCheckListRepository.save(checkList);

        // 4. 저장된 체크리스트를 UserCheckListResponse 로 변환 후 반환
        return toResponse(checkList);
    }

    public UserCheckListResponse updateContent(Long checkItemId, UserCheckListContentUpdateRequest request) {
        // 1. 체크리스트 항목 조회
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(EntityNotFoundException::new);

        // 2. 요청으로부터 받은 콘텐츠 값이 null이 아닐 경우 콘텐츠 업데이트
        if (request.content() != null) {
            checkList = checkList.updateContent(request.content());
        }

        // 3. 업데이트된 체크리스트 저장
        userCheckListRepository.save(checkList);

        // 4. 업데이트된 체크리스트를 UserCheckListResponse 로 변환 후 반환
        return toResponse(checkList);
    }

    public UserCheckListResponse updateFinish(Long checkItemId, UserCheckListFinishRequest request) {
        // 1. 체크리스트 항목 조회
        UserCheckList checkList = userCheckListRepository.findById(checkItemId)
                .orElseThrow(EntityNotFoundException::new);

        // 2. 상태 업데이트
        checkList = checkList.updateIsFinished(request.isFinished());

        // 3. 업데이트된 체크리스트 저장
        userCheckListRepository.save(checkList);

        // 4. 업데이트된 체크리스트를 UserCheckListResponse 로 변환 후 반환
        return toResponse(checkList);
    }

    // UserCheckList 객체를 UserCheckListResponse 로 변환하는 메서드
    private UserCheckListResponse toResponse(UserCheckList checkList) {
        return new UserCheckListResponse(
                checkList.getId(),
                checkList.getUser().getId(),
                checkList.getContent(),
                checkList.isFinished()
        );
    }

}
