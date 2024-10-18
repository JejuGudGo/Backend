package com.example.jejugudgo.domain.auth.basic.service;

import com.example.jejugudgo.domain.user.dto.request.FindEmailRequest;
import com.example.jejugudgo.domain.user.dto.response.FindEmailResponse;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindUserInfoService {
    private final UserRepository userRepository;

    public List<FindEmailResponse> findUserByNameAndPhone(FindEmailRequest request) {
        List<User> users = userRepository.findByPhoneNumberAndName(request.phoneNumber(), request.name());

        List<FindEmailResponse> responses = new ArrayList<>();
        for (User user : users) {
            FindEmailResponse response = new FindEmailResponse(
                    user.getEmail(),
                    user.getCreatedAt()
            );

            responses.add(response);
        }

        return responses;
    }
}
