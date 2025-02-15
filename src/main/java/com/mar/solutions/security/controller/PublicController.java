package com.mar.solutions.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Slf4j
public class PublicController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello User";
    }
}
