package com.cg.tasktracker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;



import javax.persistence.Id;



@Entity
public class AdminEntity {
	@Id
	@Column(name="admin_id")
	private String adminId;
	
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="email",unique=true, nullable=false)
	private String email;
	
	@Column(name="role", nullable=false)
	private String role;
	



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


	public String getAdminId() {
		return adminId;
	}


	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}




}
