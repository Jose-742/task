package com.task.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = Task.TABLE_NAME)
@Data
public class Task {
	public static final String TABLE_NAME = "task";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "users_id", nullable = false, updatable = false)
	private User user;

	@Column(name = "description", length = 255, nullable = false)
	@NotNull
    @NotEmpty
    @Size(min = 1, max = 255)
	private String description;

}
