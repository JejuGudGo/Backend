package com.example.jejugudgo.domain.user.user.repository;

import com.example.jejugudgo.domain.user.user.entity.Provider;
import com.example.jejugudgo.domain.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, Provider provider);

//    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByProviderAndOauthUserId(Provider provider, String userId);

    Optional<User> findUserByPhoneNumberAndName(String phoneNumber, String name);

    List<User> findByPhoneNumberAndName(String phoneNumber, String name);

    Optional<User> findByEmail(String email);
}