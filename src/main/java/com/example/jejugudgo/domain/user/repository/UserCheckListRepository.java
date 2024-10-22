package com.example.jejugudgo.domain.user.repository;

import com.example.jejugudgo.domain.user.entity.UserCheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCheckListRepository extends JpaRepository<UserCheckList, Long> {
    List<UserCheckList> findAllByUserId(Long userId);
}
