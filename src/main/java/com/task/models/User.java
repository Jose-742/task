package com.task.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = User.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
	public static final String TABLE_NAME = "user";

	@Id
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", length = 100, nullable = false, unique = true)
	@NotBlank
	@Size(min = 2, max = 100)
	private String username;

	@Column(name = "username", length = 100, nullable = false, unique = true)
	@NotBlank
	@Size(min = 8, max = 60)
	private String password;

	@OneToMany(mappedBy = "user")
	private List<Task> tasks = new ArrayList<Task>();

}
