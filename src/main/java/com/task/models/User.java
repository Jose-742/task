package com.task.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = User.TABLE_NAME)
@Data
public class User {
	public interface CreateUser{}
	public interface UpdateUser{}
	
	public static final String TABLE_NAME = "users";

	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", length = 100, nullable = false, unique = true)
	@NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
	@Size(groups = CreateUser.class, min = 2, max = 100)
	private String username;

	@JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password", length = 60, nullable = false)
    @NotNull(groups = { CreateUser.class, UpdateUser.class })
    @NotEmpty(groups = { CreateUser.class, UpdateUser.class })
    @Size(groups = { CreateUser.class, UpdateUser.class }, min = 8, max = 60)
	private String password;

	@OneToMany(mappedBy = "user")
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Task> tasks = new ArrayList<Task>();

	@ElementCollection(fetch = FetchType.EAGER)
	@JsonProperty(access = Access.WRITE_ONLY)
	@CollectionTable(name = "users_profile")
	@Column(name = "profile", nullable = false)
	private Set<Integer> profiles = new HashSet<>();

	public Set<ProfileEnum> getProfiles(){
		return this.profiles.stream().map(ProfileEnum::toEnum).collect(Collectors.toSet());
	}

	public void addProfile(ProfileEnum profileEnum){
		this.profiles.add(profileEnum.getCode());
	}
}
