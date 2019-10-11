package com.project.todo.controller;

import com.project.todo.event.ResourceCreatedEvent;
import com.project.todo.model.Todo;
import com.project.todo.service.TodoService;
import com.project.todo.service.exception.TodoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_TAREFA') and #oauth2.hasScope('read')")
    public Page<Todo> findAll(Pageable pageable) {
        return todoService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_TAREFA') and #oauth2.hasScope('read')")
    public ResponseEntity<Todo> findById(@PathVariable Long id) {
        Todo todo = todoService.findById(id);
        return todo != null ? ResponseEntity.ok(todo) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_TAREFA') and #oauth2.hasScope('write')")
    public ResponseEntity<Todo> save(@Valid @RequestBody Todo todo, HttpServletResponse response) {
        Todo todoSaved = todoService.save(todo);
        eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, todoSaved.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(todoSaved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_TAREFA') and #oauth2.hasScope('write')")
    public ResponseEntity<Todo> update(@PathVariable Long id, @Valid @RequestBody Todo todo) {
        try {
            Todo todoSaved = todoService.update(id, todo);
            return ResponseEntity.ok(todoSaved);
        } catch (TodoNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVER_TAREFA') and #oauth2.hasScope('write')")
    public void delete(@PathVariable Long id) {
        todoService.delete(id);
    }

}
