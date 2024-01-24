package com.task.services;

import java.util.Optional;

import com.task.services.exceptions.DataBindingViolationException;
import com.task.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.task.models.User;
import com.task.repositories.TaskRepository;
import com.task.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskRepository taskRepository;

	public User findById(Long id) {
		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow(
				() -> new ObjectNotFoundException("Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
	}

	@Transactional
	public User create(User user) {
		user.setId(null);
		user = this.userRepository.save(user);
		this.taskRepository.saveAll(user.getTasks());
		return user;
	}

	@Transactional
	public User update(User user) {
		User newUser = findById(user.getId());
		newUser.setPassword(user.getPassword());
		return this.userRepository.save(newUser);
	}

	public void delete(Long id) {
		findById(id);
		try {
			this.userRepository.deleteById(id);
		} catch (Exception e) {
			throw new DataBindingViolationException("Não é possivel excluir pois há entidades relacionadas!");
		}
	}

}
