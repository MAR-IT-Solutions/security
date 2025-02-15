package com.mar.solutions.security.repository;

import com.mar.solutions.security.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findByRoleIn(Set<String> roles);
}
