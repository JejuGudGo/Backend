package com.gudgo.jeju.global.util;

import com.gudgo.jeju.global.auth.nickname.repository.AdjectiveRepository;
import com.gudgo.jeju.global.auth.nickname.repository.NounRepository;
import com.gudgo.jeju.global.auth.nickname.entity.Adjective;
import com.gudgo.jeju.global.auth.nickname.entity.Noun;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class RandomNicknameUtil {
    private final UserRepository userRepository;
    private final AdjectiveRepository adjectiveRepository;
    private final NounRepository nounRepository;

    public String set() {
        String nickname;
        do {
            String adjective = getRandomAdjective();
            String noun = getRandomNoun();
            nickname = adjective + " " + noun;
        } while (userRepository.findByNickname(nickname).isPresent());

        return nickname;

    }

    private String getRandomAdjective() {
        long adjectiveSize = adjectiveRepository.count();
        long randomAdjectiveIndex = generateRandomIndex(adjectiveSize);
        return adjectiveRepository.findById(randomAdjectiveIndex)
                .map(Adjective::getAdjective)
                .orElseThrow(()-> new RuntimeException("Adjective not found"));
    }


    private String getRandomNoun() {
        long nounSize = nounRepository.count();
        long randomNounIndex = generateRandomIndex(nounSize);
        return nounRepository.findById(randomNounIndex)
                .map(Noun::getNoun)
                .orElseThrow(()-> new RuntimeException("Noun not found"));
    }

    private long generateRandomIndex(long maxNum) {
        Random random = new Random();
        return random.nextInt((int) maxNum) + 1;

    }



}