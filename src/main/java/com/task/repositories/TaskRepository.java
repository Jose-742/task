package com.task.repositories;

import com.task.models.projection.TaskProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import com.task.models.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<TaskProjection> findByUser_id(Long userId);
}
