package com.gudgo.jeju.global.util;


import com.gudgo.jeju.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class RandomNumberUtil {
    private final UserRepository userRepository;

    public Long set() {
        Random random = new Random();
        long randomNumber = random.nextLong(9000) + 1000;

        while (userRepository.findByNumberTag(randomNumber).isPresent()) {
            randomNumber = random.nextLong(9000) + 1000;
        }
        return randomNumber;
    }
}
