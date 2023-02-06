package com.cg.tasktracker.model;

import java.util.List;

import com.cg.tasktracker.entity.TaskTracker;

public class EmpTaskModel {

	private String empId;
	private List<TaskTracker> tasks;
	private long totalDuration = 0;
	private String name;
	private String message;

	public List<TaskTracker> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskTracker> task) {
		this.tasks = task;
	}

	public long getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}