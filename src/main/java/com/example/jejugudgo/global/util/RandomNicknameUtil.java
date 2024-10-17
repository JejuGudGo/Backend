package com.example.jejugudgo.global.util;

import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.data.nickname.repository.AdjectiveRepository;
import com.example.jejugudgo.global.data.nickname.repository.NounRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;
import com.example.jejugudgo.global.data.nickname.entity.Adjective;
import com.example.jejugudgo.global.data.nickname.entity.Noun;

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
                .orElseThrow(() -> new RuntimeException("Adjective not found"));
    }


    private String getRandomNoun() {
        long nounSize = nounRepository.count();
        long randomNounIndex = generateRandomIndex(nounSize);
        return nounRepository.findById(randomNounIndex)
                .map(Noun::getNoun)
                .orElseThrow(() -> new RuntimeException("Noun not found"));
    }

    private long generateRandomIndex(long maxNum) {
        Random random = new Random();
        return random.nextInt((int) maxNum) + 1;

    }
}
