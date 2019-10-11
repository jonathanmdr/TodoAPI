package com.project.todo.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SystemUser extends User {

    private static final long serialVersionUID = 1L;

    @Getter
    private com.project.todo.model.User user;

    public SystemUser(com.project.todo.model.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }

}
