package com.mar.solutions.security.controller;

import com.mar.solutions.security.dto.AuthenticationRequest;
import com.mar.solutions.security.dto.AuthenticationResponse;
import com.mar.solutions.security.dto.UserDTO;
import com.mar.solutions.security.jwt.JwtUtil;
import com.mar.solutions.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/createAdminUser")
    public UserDTO createAdminUser(@RequestBody UserDTO userDTO) {
        return userService.createAdminUser(userDTO);
    }

    @PostMapping("/createClientAdminUser")
    public UserDTO createClientAdminUser(@RequestBody UserDTO userDTO) {
        return userService.createAdminRoleUser(userDTO);
    }

    @PostMapping("/createNonAdminUser")
    public UserDTO createNonAdminUser(@RequestBody UserDTO userDTO) {
        return userService.createNonAdminUser(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @GetMapping("/getTokenDetails")
    public UserDetails getTokenDetails(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (Objects.nonNull(token)) {
            String username = jwtUtil.extractUsernameFromToken(token.replace("Bearer ", ""));
            if (Objects.nonNull(username)) {
                return userDetailsService.loadUserByUsername(username);
            }
        }
        return null;
    }
}
