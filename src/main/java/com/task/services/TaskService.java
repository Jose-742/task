package com.task.services;

import java.util.List;
import java.util.Objects;

import com.task.models.ProfileEnum;
import com.task.security.UserSpringSecurity;
import com.task.services.exceptions.AuthorizationException;
import com.task.services.exceptions.DataBindingViolationException;
import com.task.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
		Task task = this.taskRepository.findById(id).orElseThrow(
				() -> new ObjectNotFoundException("Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));

		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		if (Objects.isNull(userSpringSecurity)
				|| !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
			throw new AuthorizationException("Acesso negado!");

		return task;
	}


	public List<Task> findAllByUser(){
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		if (Objects.isNull(userSpringSecurity))
			throw new AuthorizationException("Acesso negado!");

		return this.taskRepository.findByUser_id(userSpringSecurity.getId());
	}

	@Transactional
	public Task create(Task task) {
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		if (Objects.isNull(userSpringSecurity))
			throw new AuthorizationException("Acesso negado!");

		User user = this.userService.findById(userSpringSecurity.getId());
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
			throw new DataBindingViolationException("Não é possivel excluir pois há entidades relacionadas!");
		}
	}

	private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){
		return task.getUser().getId().equals(userSpringSecurity.getId());
	}

}
