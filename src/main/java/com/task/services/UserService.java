package com.task.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.task.models.ProfileEnum;
import com.task.security.UserSpringSecurity;
import com.task.services.exceptions.AuthorizationException;
import com.task.services.exceptions.DataBindingViolationException;
import com.task.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.task.models.User;
import com.task.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	public User findById(Long id) {
		UserSpringSecurity userSpringSecurity = authenticated();
		if (!Objects.nonNull(userSpringSecurity)
				|| !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
			throw new AuthorizationException("Acesso negado!");

		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow(
				() -> new ObjectNotFoundException("Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
	}

	@Transactional
	public User create(User user) {
		user.setId(null);
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		user.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
		user = this.userRepository.save(user);
		return user;
	}

	@Transactional
	public User update(User user) {
		User newUser = findById(user.getId());
		newUser.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
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

	public static UserSpringSecurity authenticated(){
		try {
			return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e){
			return null;
		}
	}

}
