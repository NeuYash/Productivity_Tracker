package com.cg.tasktracker.model;

import java.sql.Date;
import java.sql.Timestamp;

import com.cg.tasktracker.entity.EmployeeEntity;

public class TaskTrackerModel {

	private int taskId;
	private EmployeeEntity empId;
	private Date taskDate;
	private String taskName;
	private String additionalDetails;
	private Timestamp startTime;
	private Timestamp endTime;

	public TaskTrackerModel(int taskId, EmployeeEntity empId, Date taskDate, String taskName, String additionalDetails,
			Timestamp startTime, Timestamp endTime) {
		super();
		this.taskId = taskId;
		this.empId = empId;
		this.taskDate = taskDate;
		this.taskName = taskName;
		this.additionalDetails = additionalDetails;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public TaskTrackerModel() {
		super();
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public EmployeeEntity getEmpId() {
		return empId;
	}

	public void setEmpId(EmployeeEntity empId) {
		this.empId = empId;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(String additionalDetails) {
		this.additionalDetails = additionalDetails;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

}
