package com.r3a.user.dto;

import com.r3a.user.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Role role;
    private String name;
    private String username;
}
