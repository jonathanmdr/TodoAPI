package com.project.todo.service;

import com.project.todo.model.Todo;
import com.project.todo.repository.TodoRepository;
import com.project.todo.service.exception.TodoNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public Page<Todo> findAll(Pageable pageable) {
        return todoRepository.findAll(pageable);
    }

    public Todo findById(Long id) {
        return todoRepository.findOne(id);
    }

    public Todo save(Todo todo) {
        verifyStatusForDatesUpdate(todo);
        return todoRepository.save(todo);
    }

    public Todo update(Long id, Todo todo) {
        Todo todoSaved = findTodoExistent(id);
        BeanUtils.copyProperties(todo, todoSaved, "id", "creationDate");
        verifyStatusForDatesUpdate(todoSaved);
        return todoRepository.save(todoSaved);
    }

    public void delete(Long id) {
        todoRepository.delete(id);
    }

    private Todo findTodoExistent(Long id) {
        Todo todo = todoRepository.findOne(id);

        if (todo == null) {
            throw new TodoNotFoundException();
        }

        return todo;
    }

    private void verifyStatusForDatesUpdate(Todo todoSaved) {
        switch (todoSaved.getStatus()) {
            case IN_PROGRESS:
                todoSaved.setUpdatedDate(LocalDate.now());
                todoSaved.setConclusedDate(null);
                break;
            case DONE:
                todoSaved.setUpdatedDate(LocalDate.now());
                todoSaved.setConclusedDate(LocalDate.now());
                break;
            default:
                if (todoSaved.getCreationDate() == null) {
                    todoSaved.setCreationDate(LocalDate.now());
                }
                todoSaved.setUpdatedDate(null);
                todoSaved.setConclusedDate(null);
                break;
        }
    }

}
