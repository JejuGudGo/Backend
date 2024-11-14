package com.example.jejugudgo.global.util.random;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class RandomNumberUtil {
    public Long setRandomCode() {
        Random random = new Random();
        long randomNumber = random.nextLong(900000) + 100000;
        return randomNumber;
    }
}