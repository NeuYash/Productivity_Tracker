package com.cg.tasktracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.tasktracker.entity.EmployeeEntity;
import com.cg.tasktracker.entity.TaskTracker;
import com.cg.tasktracker.exceptions.AuthenticationExceptions;
import com.cg.tasktracker.exceptions.CustomException;
import com.cg.tasktracker.model.LoginModel;
import com.cg.tasktracker.service.EmployeeServiceImpl;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "*", maxAge = 3600)
@Api
public class EmployeeController {

	@Autowired
	private EmployeeServiceImpl service;
	

	@PostMapping(value="/login")
	public ResponseEntity<EmployeeEntity> login(@RequestBody LoginModel credentials) throws AuthenticationExceptions{
		try {
			EmployeeEntity response = service.employeeLogin(credentials);
			if(response==null)
				 throw new AuthenticationExceptions("Invalid username or password");
				 
		        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		}
		catch(AuthenticationExceptions e) {
			System.out.println(e.getMessage());
			throw new AuthenticationExceptions("Invalid username or password");
		}
		 
	}
	
	@PostMapping(value="/register")
	public ResponseEntity<EmployeeEntity> signup(@RequestBody EmployeeEntity signupRequest) throws CustomException{
		EmployeeEntity response=service.employeeSignup(signupRequest);
		 try {
			if(response==null)
				 throw new CustomException("Invalid username or password");
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			throw new CustomException("Invalid username or password");
		}
			 
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping(value="/delete/{empid}")
	public ResponseEntity<EmployeeEntity> delete(@PathVariable String empid){
		EmployeeEntity response=service.deleteEmployee(empid);
		 try {
				if(response==null)
					 throw new CustomException("Employee Does not exist");
			} catch (CustomException e) {
				System.out.println(e.getMessage());
				throw new CustomException("Employee Does not exist");
			}
				 
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
	

	@GetMapping(value="fetch-tasks/{empId}")
	public ResponseEntity<List<TaskTracker>> fetchTasks(@PathVariable String empId){
		
		List<TaskTracker> tasks = service.fetchEmployeeTasks(empId);
		System.out.println("This is being called");
		return new ResponseEntity<>(tasks, HttpStatus.OK);
		
	}
	
	@GetMapping(value="fetch-employee/{empId}")
	public ResponseEntity<EmployeeEntity> fetchEmployeeDetails(@PathVariable String empId){
		return new ResponseEntity<>(service.fetchEmployee(empId), HttpStatus.OK);
	}
	
	@PutMapping(value="update-isarchived")
	public void update(@RequestBody List<String> emplist){
		 service.updateIsArchived(emplist);
	}
}
