package com.mar.solutions.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@Slf4j
public class ClientController {
    @PreAuthorize("hasRole('ROLE_CLIENT_ADMIN')")
    @GetMapping("/admin")
    public String adminAdmin() {
        return "User has ROLE_CLIENT_ADMIN";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT_ADMIN', 'ROLE_CLIENT_MANAGER')")
    @GetMapping("/adminOrManager")
    public String adminAdminOrManager() {
        return "User has ROLE_CLIENT_ADMIN or ROLE_CLIENT_MANAGER";
    }

}
