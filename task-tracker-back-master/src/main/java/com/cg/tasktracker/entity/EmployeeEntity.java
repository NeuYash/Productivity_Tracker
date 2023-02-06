package com.cg.tasktracker.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class EmployeeEntity {
	@Id
	@Column(name = "emp_id")
	private String empId;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
	private List<TaskTracker> tasks;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "role", nullable = false)
	private String role;

	@Column(name = "isArchived")
	private boolean isArchived;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public EmployeeEntity() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	public List<TaskTracker> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskTracker> tasks) {
		this.tasks = tasks;
	}

	public boolean getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

}
