package com.mar.solutions.security.configuration;

import com.mar.solutions.security.entity.UserEntity;
import com.mar.solutions.security.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
@Slf4j
public class UserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            Optional<UserEntity> user = userRepository.findByUsername(username);
            if (!user.isPresent()) {
                throw new UsernameNotFoundException("User not found");
            }
            if (!user.get().isEnabled()){
                throw new UsernameNotFoundException("User is not enabled");
            }
            String clientCode = user.get().getClientCode();
            String clientCategory;
            if (StringUtils.isEmpty(clientCode)){
                clientCategory = "ADMIN";
            } else {
                clientCategory = "CLIENT";
            }
            return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),
                    user.get().getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(
                                    "ROLE_"+clientCategory+"_"+role.getRole()
                            ))
                            .toList()
            );
        };
    }
}

