package com.example.airlineproject.security;

import com.example.airlineproject.entity.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;

@Getter
public class SpringUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public SpringUser(User user) {
        super(user.getName(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }
}