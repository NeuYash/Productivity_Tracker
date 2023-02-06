package com.cg.tasktracker.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cg.tasktracker.entity.TaskTracker;

@Repository
public interface TaskTrackerRepository extends JpaRepository<TaskTracker, Long> {

	//boolean existsByTaskName(String taskName);
	boolean existsByTaskId(Long taskId);
	List<TaskTracker> findAllByTaskName(String taskName);
	TaskTracker findByTaskId(Long taskId);
	
	/////fetch running tasks
	@Query("Select t FROM TaskTracker t WHERE t.endTime IS NULL")
	List<TaskTracker> fetchRunningTasks();
	
	@Query("Select t FROM TaskTracker t WHERE t.employee.empId =:empId AND t.endTime IS NULL")
	List<TaskTracker> fetchEmpRunningTasks(@Param(value="empId") String empId);
	
	///////////////////////
	
	/////bademployeeacctodate////////////
	@Query("Select t FROM TaskTracker t WHERE t.taskDate >=:startDate AND t.taskDate <=:endDate AND t.taskName NOT LIKE 'Break' AND t.employee.empId =:empId")
	List<TaskTracker> findBadEmpAccToDate(@Param(value="startDate") Date startDate,@Param(value="endDate") Date endDate,@Param(value="empId") String empId);
	///////////////////////////////////////
	
	@Query("Select t FROM TaskTracker t WHERE t.employee.empId =:empId AND t.endTime IS NULL ")
	TaskTracker checkRunningTask(@Param(value="empId") String empId);
	
	@Query("Select t FROM TaskTracker t WHERE t.employee.empId =:empId AND t.taskId =:taskId ")
	TaskTracker getAssignedTask(@Param(value="taskId") Long taskId, @Param(value="empId") String empId);
	
	@Query("Select t From TaskTracker t Where t.employee.empId=:empId")
	List<TaskTracker> fetchAllByEmpId(@Param(value="empId")String empId);
	
	@Query("Select t From TaskTracker t Where t.employee.empId=:empId AND t.taskName =:taskName")
	List<TaskTracker> fetchAllByEmpIdandTaskName(@Param(value="empId")String empId,@Param(value="taskName") String taskName);
	
	@Query("Select t FROM TaskTracker t WHERE t.taskDate =:date AND t.endTime >=:endTime")
	List<TaskTracker> findBadTaskByDate(@Param(value="date")Date date, @Param(value="endTime")Timestamp endTime);
	
	@Query("Select t FROM TaskTracker t WHERE (t.taskDate =:date AND t.endTime !=:endTime) AND (t.duration>14400000 OR (t.taskName LIKE 'Break' AND t.duration>3600000))")
	List<TaskTracker> findBadTask(@Param(value="date")Date date,@Param(value="endTime")Timestamp endTime);
	 
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Query("Select t FROM TaskTracker t WHERE t.taskDate >=:startDate AND t.taskDate <=:endDate")
	List<TaskTracker> getTaskAccToDateN(@Param(value="startDate") Date startDate,@Param(value="endDate") Date endDate);
	
	@Query("Select t FROM TaskTracker t WHERE t.employee.empId =:empId AND t.taskDate >=:startDate AND t.taskDate <=:endDate") //search by empid and date
	List<TaskTracker> searchTasksN(@Param(value="startDate") Date startDate,@Param(value="endDate") Date endDate, @Param(value="empId") String empId);
	
	@Query("Select t FROM TaskTracker t WHERE t.taskName =:taskName AND t.taskDate >=:startDate AND t.taskDate <=:endDate") //search by taskName and date
	List<TaskTracker> searchTaskNamebyDateN(@Param(value="taskName") String taskName,@Param(value="startDate") Date startDate,@Param(value="endDate") Date endDate);
	
	@Query("Select t FROM TaskTracker t WHERE t.employee.empId =:empId AND t.taskName =:taskName AND t.taskDate >=:startDate AND t.taskDate <=:endDate") //search by empId,TaskName and Date
	List<TaskTracker> searchbyEmpIdTaskNameDateN(@Param(value="empId") String empId,@Param(value="taskName") String taskName,@Param(value="startDate") Date startDate,@Param(value="endDate") Date endDate);
	
	
	
}
