package com.mar.solutions.security.service;

import com.mar.solutions.security.dto.UserDTO;
import com.mar.solutions.security.entity.RoleEntity;
import com.mar.solutions.security.entity.UserEntity;
import com.mar.solutions.security.repository.RoleRepository;
import com.mar.solutions.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        BeanUtils.copyProperties(userDTO, userEntity);
        List<RoleEntity> roles = roleRepository.findByRoleIn(userDTO.getRoles());
        userEntity.setRoles(roles);
        UserEntity savedUser = userRepository.save(userEntity);
        userDTO.setPassword(null);
        return userDTO;
    }

}
