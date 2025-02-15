package com.mar.solutions.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    @PreAuthorize("hasAuthority('ROLE_ADMIN_ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello User";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN_ADMIN')")
    @GetMapping("/role")
    public String role() {
        return "Hello Role";
    }
}
