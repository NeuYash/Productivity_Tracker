package com.cg.tasktracker.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.tasktracker.dao.EmployeeRepository;
import com.cg.tasktracker.dao.TaskTrackerRepository;
import com.cg.tasktracker.entity.EmployeeEntity;
import com.cg.tasktracker.entity.TaskTracker;
import com.cg.tasktracker.exceptions.CustomException;
import com.cg.tasktracker.model.EmpTaskModel;

@Service
public class TaskTrackerService {

	@Autowired
	TaskTrackerRepository repo;

	@Autowired
	EmployeeRepository empRepo;

	public TaskTracker addTask(TaskTracker tasktracker) {

		tasktracker.setTaskDate(new java.sql.Date(System.currentTimeMillis()));
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.HOUR_OF_DAY) < 8 || cal.get(Calendar.HOUR_OF_DAY) >= 20) // time set for 8 to 20 which is
																						// 12 hours
			throw new CustomException("Task cannot be added other than office hours");
		tasktracker.setStartTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("task with name " + tasktracker.getTaskName() + " added by employee "
				+ tasktracker.getEmployee().getName());
		return repo.save(tasktracker);
	}

	public TaskTracker endTask(TaskTracker task) {
		if (repo.existsByTaskId(task.getTaskId())) {

			task = repo.getAssignedTask(task.getTaskId(), task.getEmployee().getEmpId());

			if (task == null) {
				throw new CustomException("Task is not assigned to particular user");
			} else if (task.getDuration() != 0) {
				throw new CustomException("Task already ended");
			}
//			Calendar cal = Calendar.getInstance();
//			if (cal.get(Calendar.HOUR) >= 18) {
//				cal.setTimeInMillis(task.getStartTime().getTime());
//				System.out.println(cal);
//				cal.set(Calendar.HOUR, 18);
//				cal.set(Calendar.MINUTE, 1);
//				cal.set(Calendar.SECOND, 0);
//			}
//			if (cal.get(Calendar.HOUR) < 9)
//				throw new CustomException("Task cannot end before office starts");

			task.setEndTime(new Timestamp(System.currentTimeMillis()));
			System.out.println("end time:" + task.getEndTime());
			task.setDuration(task.getEndTime().getTime() - task.getStartTime().getTime());

			return repo.save(task);
		}
		throw new CustomException("Task not found");
	}

	public List<TaskTracker> logout(String empId) {
		List<TaskTracker> runningTasks = repo.fetchEmpRunningTasks(empId);
		if (runningTasks == null)
			return null;
		else {
			for (TaskTracker task : runningTasks) {
				endTask(task);
			}
			return runningTasks;
		}
	}

	public boolean isTaskAlreadyRunning(TaskTracker task) {
		TaskTracker taskk = repo.checkRunningTask(task.getEmployee().getEmpId());
		System.out.println(taskk);
		if (taskk != null)
			return true;
		return false;
	}

	public List<TaskTracker> getTasksByDate(Date startDate, Date endDate) { // search tasks by date
		return repo.getTaskAccToDateN(startDate, endDate);
	}

	public List<TaskTracker> searchEmpTasks(String empid, Date sdate, Date edate) { // search tasks by empid and date
		return repo.searchTasksN(sdate, edate, empid);
	}

	public List<TaskTracker> searchTaskNamebyDate(String taskName, Date sdate, Date edate) { // search tasks by taskname
																								// and date
		return repo.searchTaskNamebyDateN(taskName, sdate, edate);
	}

	public List<TaskTracker> searchbyEmpIdTaskNameDate(String empid, String taskName, Date sdate, Date edate) { // search
																												// tasks
																												// by
																												// empid,taskname,dates
																												// //
																												// tasks
		return repo.searchbyEmpIdTaskNameDateN(empid, taskName, sdate, edate);
	}

	public List<TaskTracker> searchbyEmpIDTaskName(String empid, String taskName) { // search tasks by empid and
																					// taskName
		return repo.fetchAllByEmpIdandTaskName(empid, taskName);
	}

	public List<TaskTracker> getTasksByName(String taskName) {
		return repo.findAllByTaskName(taskName);
	}

//	public void setEndTime() throws ParseException { // call this function on ngOnInit() which will end the tasks
//														// running after office hours
//		System.out.println("SetEndTime1 called in TaskService");
//		List<TaskTracker> runningTasks = repo.fetchRunningTasks();
//		Timestamp current = new Timestamp(System.currentTimeMillis());
//		Date date = new Date();
//		SimpleDateFormat dt1 = new SimpleDateFormat("yyyyy-MM-dd");
//		date = new SimpleDateFormat("yyyy-MM-dd").parse(dt1.format(date));
//
//		for (TaskTracker task : runningTasks) {
//			System.out.println(task.getTaskDate().compareTo(date));
//			System.out.println(task.getTaskDate());
//			System.out.println(date);
//			if ((task.getTaskDate().compareTo(date) == -1)
//					|| (task.getTaskDate().compareTo(date) == 0 && current.getHours() >= 20)) {
//
//				Timestamp endTime = new Timestamp(task.getStartTime().getTime());
//				endTime.setHours(20);
//				endTime.setMinutes(1);
//				endTime.setSeconds(0);
//				endTime.setNanos(0);
//
//				task.setEndTime(endTime);
//				task.setDuration(task.getEndTime().getTime() - task.getStartTime().getTime());
//				repo.save(task);
//			}
//		} // for
//
//	}
	public void setEndTime() throws ParseException { // call this function on ngOnInit() which will end the tasks
		// running after office hours
		System.out.println("SetEndTime1 called in TaskService");
		List<TaskTracker> runningTasks = repo.fetchRunningTasks();
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyyy-MM-dd");
		date = new SimpleDateFormat("yyyy-MM-dd").parse(dt1.format(date));

		for (TaskTracker task : runningTasks) {
			if ((task.getTaskDate().compareTo(date) == -1)
					|| (task.getTaskDate().compareTo(date) == 0 && cal.get(Calendar.HOUR_OF_DAY) >= 20)) {

				cal.setTimeInMillis(task.getStartTime().getTime());
				cal.set(Calendar.HOUR, 20);
				cal.set(Calendar.MINUTE, 1);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				task.setEndTime(new Timestamp(cal.getTimeInMillis()));
				task.setDuration(task.getEndTime().getTime() - task.getStartTime().getTime());
				repo.save(task);
			}
		} // for
	}

	public List<EmpTaskModel> getBadTask(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 20);
		cal.set(Calendar.MINUTE, 1);
		cal.set(Calendar.SECOND, 0);
		System.out.println(new Timestamp(cal.getTimeInMillis()));
		Timestamp time = new Timestamp(cal.getTimeInMillis());
		System.out.println(time);
		List<TaskTracker> badTasks = repo.findBadTaskByDate(date, time);
		for (TaskTracker t : repo.findBadTask(date, time)) { // add task which is running for more than 4 hours and (add
																// break
			// which is running for more than 1 hour)
			badTasks.add(t);
		}
		List<EmpTaskModel> empBadTasks = new ArrayList<EmpTaskModel>();
		int length = 0;
		int firstFlag = 0;
		for (TaskTracker badTask : badTasks) {
			int flag = 0;
			for (int i = 0; i <= length; i++) {
				if (empBadTasks.isEmpty()) {
					empBadTasks.add(addBadTask(badTask));
					flag = 1;
				} else if (length != 0 || firstFlag == 1) {
					if (badTask.getEmployee().getEmpId() == empBadTasks.get(i).getEmpId()) {
						EmpTaskModel model = empBadTasks.get(i);
						model.setTotalDuration(model.getTotalDuration() + badTask.getDuration());
						List<TaskTracker> tasks = model.getTasks();
						tasks.add(badTask);
						model.setTasks(tasks);
						empBadTasks.set(i, model);
						flag = 1;
					}
				}
			}
			if (flag == 0) {
				empBadTasks.add(addBadTask(badTask));
				length = length + 1;
			}
			if (empBadTasks.size() == 1) {
				firstFlag = 1;
			}
			flag = 0;
		}
		System.out.println(empBadTasks);

		return empBadTasks;
	}

	public EmpTaskModel addBadTask(TaskTracker badTask) {
		EmpTaskModel badEmpTask = new EmpTaskModel();
		badEmpTask.setEmpId(badTask.getEmployee().getEmpId());
		badEmpTask.setName(badTask.getEmployee().getName());
		if (badTask.getTaskName().equals("Break") && badTask.getDuration() >= 3600000) {// if taskName is Break and is
																						// running for more than 1 hours
			badEmpTask.setMessage("Break longer than 1 hour");
		} else if (badTask.getDuration() > 14400000)
			badEmpTask.setMessage("Task running more than 4 hours");
		else
			badEmpTask.setMessage("Task running after office hours");
		badEmpTask.setTotalDuration(badTask.getDuration());
		List<TaskTracker> badTasks = new ArrayList<TaskTracker>();
		badTasks.add(badTask);
		badEmpTask.setTasks(badTasks);

		return badEmpTask;
	}

	public List<EmpTaskModel> badEmployeeAccToDate(Date startDate, Date endDate) {
		long days = TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
		if (days < 0)
			throw new CustomException("End date should be after Start date");
		else if (days == 0)
			days = 1; // start date and end date same
		long durationCut = 34200000 * days; // 9.5 hours per day
		List<EmpTaskModel> EmpTaskModel = new ArrayList<>();
		int count = 0;
		for (EmployeeEntity emp : empRepo.findAll()) {
			EmpTaskModel bad = new EmpTaskModel();
			List<TaskTracker> badTask = new ArrayList<>();

			bad.setTotalDuration(0);
			count = 0;
			List<TaskTracker> badEmpTasks = repo.findBadEmpAccToDate(startDate, endDate, emp.getEmpId());
			for (TaskTracker task : badEmpTasks) {
				System.out.println("there is a task with an employee today");
				bad.setTotalDuration(bad.getTotalDuration() + task.getDuration());
				badTask.add(task);
				count += 1;
			} // for
			bad.setEmpId(emp.getEmpId());
			bad.setTasks(badTask);
			bad.setName(emp.getName());

			if (count == 0) {
				bad.setMessage("No Task Added");
				EmpTaskModel.add(bad); // only if employee has not done any task then he is an bad employee
			}
			if (count == 1 && bad.getTotalDuration() < durationCut) {
				bad.setMessage("Only one task added");
				EmpTaskModel.add(bad);
			}
			if (count > 1 && bad.getTotalDuration() < durationCut) {
				bad.setMessage("TotalDuration less than " + (days * 9.5) + " hours for " + days + " days");
				EmpTaskModel.add(bad);
			}

		} // for
			// return badEmployee;
		return EmpTaskModel;
	}
}
