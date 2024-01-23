package com.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
