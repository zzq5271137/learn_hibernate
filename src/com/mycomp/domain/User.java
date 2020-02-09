package com.mycomp.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User {
    private Long user_id;
    private String user_code;
    private String user_name;
    private String user_password;
    private Character user_state;

    // 一个user可以有多个role
    private Set<Role> roles = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_code='" + user_code + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_state=" + user_state +
                ", roles=" + roles +
                '}';
    }
}
