package com.example.jejugudgo.domain.user.checkList.repository;

import com.example.jejugudgo.domain.user.checkList.entity.UserCheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCheckListRepository extends JpaRepository<UserCheckList, Long> {
    List<UserCheckList> findAllByUserId(Long userId);

    Optional<UserCheckList> findTopByUserIdOrderByOrderNumberDesc(Long userId);
}
