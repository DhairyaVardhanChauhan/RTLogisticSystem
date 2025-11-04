package com.dvc.RtLogistics.repository;

import com.dvc.RtLogistics.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,String> {

    Optional<User> findByUserName(String username);
}
