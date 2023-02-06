package com.cg.tasktracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.tasktracker.dao.AdminRepository;
import com.cg.tasktracker.entity.AdminEntity;
import com.cg.tasktracker.model.LoginModel;

@Service
public class AdminService{

	@Autowired
	AdminRepository adminDao;
	
	public AdminEntity adminSignup(AdminEntity adminModel) {
		if(adminDao.existsByEmail(adminModel.getEmail())) {
			return null;
		}
		adminDao.save(adminModel);
		
		return adminModel;
		
	}

	public AdminEntity adminLogin(LoginModel credentials) {
		AdminEntity emp = adminDao.findByEmail(credentials.getEmail());
		if(emp.getPassword().equals(credentials.getPassword())) {
			return emp;
		}
		return null;
	}
}
