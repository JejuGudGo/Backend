package com.gudgo.jeju.domain.user.repository;

import com.gudgo.jeju.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, String provider);

    Optional<User> findByNumberTag(Long numberTag);

//    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);

    Optional<User> findUserByPhoneNumberAndName(String phoneNumber, String name);
}
