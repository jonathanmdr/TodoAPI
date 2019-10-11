package com.project.todo.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

@Getter
public class ResourceCreatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    private Long id;

    public ResourceCreatedEvent(Object resource, HttpServletResponse response, Long id) {
        super(resource);

        this.response = response;
        this.id = id;
    }

}
