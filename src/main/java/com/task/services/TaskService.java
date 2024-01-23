package com.task.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.task.models.Task;
import com.task.models.User;
import com.task.repositories.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserService userService;

	public Task findById(Long id) {
		Optional<Task> task = this.taskRepository.findById(id);
		return task.orElseThrow(
				() -> new RuntimeException("Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));
	}

	@Transactional
	public Task create(Task task) {
		User user = this.userService.findById(task.getUser().getId());
		task.setId(null);
		task.setUser(user);
		return this.taskRepository.save(task);
	}

	@Transactional
	public Task update(Task task) {
		Task newTask = findById(task.getId());
		newTask.setDescription(task.getDescription());
		return this.taskRepository.save(newTask);
	}

	public void delete(Long id) {
		findById(id);
		try {
			this.taskRepository.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException("Não é possivel excluir pois há entidades relacionadas!");
		}
	}

}
