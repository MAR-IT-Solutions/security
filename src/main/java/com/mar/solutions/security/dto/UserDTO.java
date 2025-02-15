package com.mar.solutions.security.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
@Getter
@Setter
public class UserDTO {
    private String clientCode;
    private String username;
    private boolean enabled;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private String password;
    private Set<String> roles;
}
