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
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody TodoCreateRequestDto requestDto) {
        todoService.create(request, requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "")
    public ResponseEntity<List<TodoResponseDto>> getByType(@RequestParam("type") TodoType type, HttpServletRequest request) {
        return ResponseEntity.ok(todoService.getByType(type, request));
    }

    @GetMapping(value = "/{id}")
    public Todo getById(@PathVariable("id") Long id) {
        return todoService.getById(id);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TodoUpdateRequestDto requestDto) {
        todoService.update(id, requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}/complete")
    public ResponseEntity<?> updateComplete(@PathVariable("id") Long id) {
        todoService.finish(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        todoService.delete(id);
        return ResponseEntity.ok().build();
    }
}
