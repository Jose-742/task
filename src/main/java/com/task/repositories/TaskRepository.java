package com.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.models.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser_id(Long userId);
}
