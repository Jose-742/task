package com.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.models.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Transactional(readOnly = true)
    User findByUsername(String username);
}
