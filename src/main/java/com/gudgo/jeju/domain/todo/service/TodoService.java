package com.gudgo.jeju.domain.todo.service;

import com.gudgo.jeju.domain.todo.dto.request.TodoCreateRequestDto;
import com.gudgo.jeju.domain.todo.dto.response.TodoResponseDto;
import com.gudgo.jeju.domain.todo.entity.Todo;
import com.gudgo.jeju.domain.todo.entity.TodoType;
import com.gudgo.jeju.domain.todo.repository.TodoRepository;
import com.gudgo.jeju.domain.todo.dto.request.TodoUpdateRequestDto;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;
    private final UserRepository userRepository;

    @Transactional
    public TodoResponseDto create(HttpServletRequest request, TodoCreateRequestDto requestDto) {
        Todo todo = Todo.builder()
                .user(getUser(request))
                .type(TodoType.TODO)
                .content(requestDto.content())
                .isFinished(false)
                .isDeleted(false)
                .build();

        todoRepository.save(todo);

        TodoResponseDto response = new TodoResponseDto(todo.getId(), todo.getType(), todo.getOrderNumber(), todo.getContent(), todo.isFinished());
        return response;
    }

    public List<TodoResponseDto> getByType(TodoType type, HttpServletRequest request) {
        Long userId = getUser(request).getId();
        List<TodoResponseDto> todoList = todoRepository.findByTypeAndUserIdAndIsDeletedFalseOrderByOrderNumber(type, userId);
        return todoList;
    }

    public Todo getById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void update(Long id, TodoUpdateRequestDto requestDto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        if (requestDto.type() != null) {
            todo = todo.withType(requestDto.type());
        }
        if (requestDto.orderNumber() != null) {
            todo = todo.withOrderNumber(requestDto.orderNumber());
        }
        if (requestDto.content() != null) {
            todo = todo.withContent(requestDto.content());
        }

        todoRepository.save(todo);
    }

    @Transactional
    public void updateStatus(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        if (todo.isFinished()) todo = todo.withIsFinished(false);
        else todo = todo.withIsFinished(true);

        todoRepository.save(todo);
    }

    @Transactional
    public void delete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Todo removeTodo = todo.withDeleted(true);
        todoRepository.save(removeTodo);
    }


    private User getUser(HttpServletRequest request) {
        String token = tokenExtractor.getAccessTokenFromHeader(request);    // 요청 헤더에서 AccessToken 추출
        Long userid = subjectExtractor.getUserIdFromToken(token);           // 토큰에서 userid 추출

        return userRepository.findById(userid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userid));
    }
}

