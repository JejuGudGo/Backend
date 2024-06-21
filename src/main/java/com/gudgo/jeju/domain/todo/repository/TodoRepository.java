package com.gudgo.jeju.domain.todo.repository;

import com.gudgo.jeju.domain.todo.dto.response.TodoResponseDto;
import com.gudgo.jeju.domain.todo.entity.Todo;
import com.gudgo.jeju.domain.todo.entity.TodoType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<TodoResponseDto> findByTypeAndUserIdAndIsDeletedFalseOrderByOrderNumber(TodoType type, Long userId);
}
