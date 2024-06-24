package com.gudgo.jeju.domain.planner.service;

import com.gudgo.jeju.domain.planner.cache.MessageCaching;
import com.gudgo.jeju.domain.planner.dto.request.chatting.MessageRequest;
import com.gudgo.jeju.domain.planner.dto.response.MessageResponse;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.util.image.entity.Category;
import com.gudgo.jeju.global.util.image.service.ImageUpdateService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.EncoderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final UserRepository userRepository;
    private final ImageUpdateService imageUpdateService;
    private final MessageCaching messageCaching;

    @Transactional
    public MessageResponse sendMessage(Long chatRoomId, MultipartFile[] images, MessageRequest request) throws Exception {
        User user = userRepository.findById(request.userId())
                .orElseThrow(EncoderException::new);

        MessageResponse response =  new MessageResponse(
                request.userId(),
                user.getNickname(),
                user.getNumberTag(),
                user.getProfile().getProfileImageUrl(),
                request.message(),
                getImageUrls(request.userId(), images)
        );

        messageCaching.saveMessageToRedis(chatRoomId, response);

        return response;
    }

    private List<String> getImageUrls(Long userId, MultipartFile[] images) throws Exception {
        List<String> imageUrls = new ArrayList<>();
        for(MultipartFile image : images) {
            String fileImageUrl = imageUpdateService.saveImage(userId, image, Category.CHAT).toString();
            imageUrls.add(fileImageUrl);
        }

        return imageUrls;
    }
}
