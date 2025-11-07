package com.dvc.RtLogistics.repository;

import com.dvc.RtLogistics.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<UserRole,Long> {
    Optional<UserRole> findByName(String name);
}
