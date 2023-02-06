package com.cg.tasktracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.tasktracker.dao.EmployeeRepository;
import com.cg.tasktracker.dao.TaskTrackerRepository;
import com.cg.tasktracker.entity.EmployeeEntity;
import com.cg.tasktracker.entity.TaskTracker;
import com.cg.tasktracker.exceptions.CustomException;
import com.cg.tasktracker.model.LoginModel;

@Service
public class EmployeeServiceImpl {

	@Autowired
	EmployeeRepository employeeDao;

	@Autowired
	TaskTrackerRepository taskRepo;

	public EmployeeEntity employeeSignup(EmployeeEntity employeeModel) {
		if (employeeDao.existsByEmail(employeeModel.getEmail())) {
			employeeModel.setIsArchived(Boolean.TRUE); // if that particular name is present in new list being uploaded
														// then set isArchived true
		} else
			employeeModel.setIsArchived(Boolean.TRUE); // if completely new employee then set isArchive true
		employeeDao.save(employeeModel);
		return employeeModel;
	}

	public EmployeeEntity employeeLogin(LoginModel credentials) {
		EmployeeEntity emp = employeeDao.findByEmail(credentials.getEmail());

		if (emp.getIsArchived() == Boolean.TRUE) { // if he is not alloted any project, only then he can login
			if (emp.getPassword().equals(credentials.getPassword())) {
				return emp;
			}
			return null;
		} // main if
		else
			throw new CustomException("Employee is Archived");

	}

	public EmployeeEntity deleteEmployee(String empid) {
		EmployeeEntity emp = employeeDao.findByEmpId(empid);
		employeeDao.delete(emp);
		return emp;
	}

	public List<TaskTracker> fetchTasks(String empId) {
		return taskRepo.fetchAllByEmpId(empId);
	}

	public EmployeeEntity fetchEmployee(String empId) {
		EmployeeEntity emp = employeeDao.findByEmpId(empId);
		return emp;
	}

	public List<TaskTracker> fetchEmployeeTasks(String empId) {
		return taskRepo.fetchAllByEmpId(empId);
	}

	public void updateIsArchived(List<String> emplist) {
		for (EmployeeEntity e : employeeDao.findAll()) {
			if (emplist.contains(e.getEmpId()))
				e.setIsArchived(true);
			else {
				e.setIsArchived(false);
				employeeDao.save(e);
			} // else
		}
	}
}// class
