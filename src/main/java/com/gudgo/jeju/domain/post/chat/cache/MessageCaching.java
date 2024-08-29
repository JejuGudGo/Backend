package com.gudgo.jeju.domain.post.chat.cache;

import com.gudgo.jeju.domain.post.chat.dto.response.MessageResponse;
import com.gudgo.jeju.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MessageCaching {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisUtil redisUtil;

    private static final String CHAT_KEY_PREFIX = "chat:";

    public List<String> getAllChatKeys() {
        Set<String> keys = redisTemplate.keys(CHAT_KEY_PREFIX + "*");
        return new ArrayList<>(keys);
    }

    public List<MessageResponse> getAllMessagesFromRedis(Long chatRoomId) {
        String key = CHAT_KEY_PREFIX + chatRoomId;
        List<Object> messages = redisUtil.getAllObjectsData(key);
        List<MessageResponse> messageResponses = new ArrayList<>();
        for (Object message : messages) {
            if (message instanceof MessageResponse) {
                messageResponses.add((MessageResponse) message);
            }
        }
        return messageResponses;
    }

    public void saveMessageToRedis(Long chatRoomId, MessageResponse message) {
        String key = CHAT_KEY_PREFIX + chatRoomId;
        redisUtil.setObjectData(key, message);
    }

    public void deleteMessageFromRedis(Long chatRoomId) {
        String key = CHAT_KEY_PREFIX + chatRoomId;
        redisUtil.deleteData(key);
    }
}
