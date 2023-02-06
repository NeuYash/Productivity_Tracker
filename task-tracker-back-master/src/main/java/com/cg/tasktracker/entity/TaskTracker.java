package com.cg.tasktracker.entity;

 

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="tasks")
@Entity
public class TaskTracker {

 

    @Id
    @Column(name="task_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long taskId;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    private EmployeeEntity employee;

    @Column(name = "task_date",nullable=false)
    private Date taskDate;
    
    @Column(name = "task_name", nullable = false)
    private String taskName;
    
    @Column(name = "additional_detail", nullable = false, length = 6000)
    private String additionalDetails;
    
    @Column(name = "start_time",nullable=false)
    private Timestamp startTime;
    
    @Column(name = "end_time")
    private Timestamp endTime;
    
    @Column(name="duration")
    long duration;


    public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public TaskTracker() {
        super();
    }


	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public EmployeeEntity getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeEntity employee) {
		this.employee = employee;
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

	@Override
	public String toString() {
		return "Tasktracker [taskId=" + taskId + ", empId=" + employee + ", taskDate=" + taskDate + ", taskName="
				+ taskName + ", additionalDetails=" + additionalDetails + ", startTime=" + startTime + ", endTime="
				+ endTime + "]";
	}

 

}