package com.gudgo.jeju.domain.todo.controller;

import com.gudgo.jeju.domain.todo.dto.request.TodoCreateRequestDto;
import com.gudgo.jeju.domain.todo.dto.response.TodoResponseDto;
import com.gudgo.jeju.domain.todo.entity.Todo;
import com.gudgo.jeju.domain.todo.entity.TodoType;
import com.gudgo.jeju.domain.todo.service.TodoService;
import com.gudgo.jeju.domain.todo.dto.request.TodoUpdateRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/todo")
@RequiredArgsConstructor
@Slf4j
@RestController
public class TodoController {

    private final TodoService todoService;

    @PostMapping(value = "")
    public ResponseEntity<TodoResponseDto> create(HttpServletRequest request, @RequestBody TodoCreateRequestDto requestDto) {
        TodoResponseDto response = todoService.create(request, requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<TodoResponseDto>> getByType(HttpServletRequest request) {
        return ResponseEntity.ok(todoService.getByType(TodoType.TODO, request));
    }

    @GetMapping(value = "/{todoId}")
    public Todo getById(@PathVariable("todoId") Long todoId) {
        return todoService.getById(todoId);
    }

    @PatchMapping(value = "/{todoId}")
    public ResponseEntity<?> update(@PathVariable("todoId") Long todoId, @RequestBody TodoUpdateRequestDto requestDto) {
        todoService.update(todoId, requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{todoId}/complete")
    public ResponseEntity<?> updateComplete(@PathVariable("todoId") Long todoId) {
        todoService.updateStatus(todoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{todoId}")
    public ResponseEntity<?> delete(@PathVariable("todoId") Long todoId) {
        todoService.delete(todoId);
        return ResponseEntity.ok().build();
    }
}
