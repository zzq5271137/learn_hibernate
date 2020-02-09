package com.mycomp.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Role {
    private Long role_id;
    private String role_name;
    private String role_memo;

    // 一个角色下面可以有多个用户
    private Set<User> users = new HashSet<>();

    @Override
    public String toString() {
        return "Role{" +
                "role_id=" + role_id +
                ", role_name='" + role_name + '\'' +
                ", role_memo='" + role_memo + '\'' +
                // ", users='" + users + '\'' +
                '}';
    }
}
