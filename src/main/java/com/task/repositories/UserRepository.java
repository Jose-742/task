package com.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
