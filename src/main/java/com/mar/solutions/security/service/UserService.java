package com.mar.solutions.security.service;

import com.mar.solutions.security.dto.UserDTO;
import com.mar.solutions.security.entity.RoleEntity;
import com.mar.solutions.security.entity.UserEntity;
import com.mar.solutions.security.repository.RoleRepository;
import com.mar.solutions.security.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createAdminUser(UserDTO userDTO) {
        userDTO.setClientCode("ADMIN");
        return createAdminRoleUser(userDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN_ADMIN')")
    public UserDTO createAdminRoleUser(UserDTO userDTO) {

        if (StringUtils.isEmpty(userDTO.getClientCode())) {
            throw new RuntimeException("Client code is required");
        }
        List<UserEntity> userEntities = userRepository.findByClientCode(userDTO.getClientCode());
        if (!CollectionUtils.isEmpty(userEntities)) {
            throw new RuntimeException("Admin user already exists for " + userDTO.getClientCode());
        }
        if (CollectionUtils.isEmpty(userDTO.getRoles())) {
            userDTO.setRoles(Set.of("ADMIN"));
        } else {
            userDTO.getRoles().add("ADMIN");
        }
        return createUser(userDTO);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN_ADMIN', 'ROLE_CLIENT_ADMIN')")
    public UserDTO createNonAdminUser(UserDTO userDTO) {
        //TODO Client Code to be retrieve from JWT token
        if (StringUtils.isEmpty(userDTO.getClientCode())) {
            throw new RuntimeException("Client code is required");
        }
        if (CollectionUtils.isEmpty(userDTO.getRoles())) {
            throw new RuntimeException("Roles are required");
        } else if (userDTO.getRoles().contains("ADMIN")) {
            throw new RuntimeException("ADMIN Role is not allowed");
        }
        return createUser(userDTO);
    }

    private UserDTO createUser(UserDTO userDTO) {
        HashSet<RoleEntity> roles = new HashSet<>();
        if (StringUtils.isEmpty(userDTO.getUsername())) {
            throw new RuntimeException("Username is required");
        }
        if (StringUtils.isEmpty(userDTO.getPassword())) {
            throw new RuntimeException("Password is required");
        }
        if (CollectionUtils.isEmpty(userDTO.getRoles())) {
            throw new RuntimeException("Roles are required");
        } else {
            for (String role : userDTO.getRoles()) {
                RoleEntity roleEntity = roleRepository.findByRole(role);
                if (roleEntity == null) {
                    throw new RuntimeException("Role " + role + " not found");
                } else {
                    roles.add(roleEntity);
                }
            }
        }
        UserEntity userEntity = new UserEntity();
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        BeanUtils.copyProperties(userDTO, userEntity);
        userEntity.setRoles(roles.stream().toList());
        userRepository.save(userEntity);
        userDTO.setPassword(null);
        return userDTO;
    }
}
