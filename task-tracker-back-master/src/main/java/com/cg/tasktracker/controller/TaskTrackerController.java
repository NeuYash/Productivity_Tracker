package com.cg.tasktracker.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.tasktracker.entity.TaskTracker;
import com.cg.tasktracker.exceptions.CustomException;
import com.cg.tasktracker.model.EmpTaskModel;
import com.cg.tasktracker.service.TaskTrackerService;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin("*")
@RequestMapping("/tasktracker")
@Api
public class TaskTrackerController {

	@Autowired
	private TaskTrackerService service;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaskTracker> addTask(@RequestBody TaskTracker task) throws CustomException {

		try {
			if (service.isTaskAlreadyRunning(task))
				throw new CustomException("Task already running");
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			throw new CustomException("Task already running");
		}
		TaskTracker response = service.addTask(task);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaskTracker> endTask(@RequestBody TaskTracker task) throws CustomException {
		TaskTracker response = service.endTask(task);
		try {
			if (response == null)
				throw new CustomException("error while ending task");
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			throw new CustomException("error while ending task");
		}

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
	
	@PutMapping(path="/logout/{empId}")
	public ResponseEntity<List<TaskTracker>> logout(@PathVariable String empId){
		return new ResponseEntity<>(service.logout(empId), HttpStatus.ACCEPTED);
	}
	
	@GetMapping(path = "/fetch-tasks-by-date/{sdate}/{edate}")
	public ResponseEntity<List<TaskTracker>> fetchTasksByDate(@PathVariable Date sdate, @PathVariable Date edate)
			throws CustomException {
		System.out.println("called sdate=" + sdate + " edate=" + edate);
		List<TaskTracker> task = service.getTasksByDate(sdate, edate);
		try {
			if (task == null)
				throw new CustomException("no task present");
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			throw new CustomException("error while ending task");
		}

		return new ResponseEntity<>(task, HttpStatus.ACCEPTED);
	}

	@GetMapping(path = "/search-emp-tasks-by-date/{empid}/{sdate}/{edate}")
	public ResponseEntity<List<TaskTracker>> fetchEmpTasksByDate(@PathVariable String empid, @PathVariable Date sdate,
			@PathVariable Date edate) {
		List<TaskTracker> task = service.searchEmpTasks(empid, sdate, edate);
		try {
			if (task == null)
				throw new CustomException(
						"employee has not performed any task between dates : " + sdate + " and " + edate);
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			throw new CustomException("employee has not performed any task between dates : " + sdate + " and " + edate);
		}

		return new ResponseEntity<>(task, HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/fetch-tasks-by-name/{taskName}")
	public ResponseEntity<List<TaskTracker>> fetchTasksByName(@PathVariable String taskName) throws CustomException {
		List<TaskTracker> task = service.getTasksByName(taskName);
		try {
			if (task == null)
				throw new CustomException("no task present");
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			throw new CustomException("no task present");
		}

		return new ResponseEntity<>(task, HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/fetch-by-name-and-date/{taskname}/{sdate}/{edate}")
	public ResponseEntity<List<TaskTracker>> searchTaskNamebyDate(@PathVariable String taskname,
			@PathVariable String sdate, @PathVariable String edate) {
		Date convertedsDate = null;
		Date convertedeDate = null;
		try {
			convertedsDate = new SimpleDateFormat("yyyy-MM-dd").parse(sdate);
			convertedeDate = new SimpleDateFormat("yyyy-MM-dd").parse(edate);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}

		List<TaskTracker> task = service.searchTaskNamebyDate(taskname, convertedsDate, convertedeDate);
		try {
			if (task == null)
				throw new CustomException(
						"no task by name " + taskname + " between dates : " + sdate + " and " + edate);
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			throw new CustomException("no task by name " + taskname + " between dates : " + sdate + " and " + edate);
		}
		return new ResponseEntity<>(task, HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/fetch-by-empid-and-name/{empid}/{taskname}")
	public ResponseEntity<List<TaskTracker>> fetchByEmpIdAndName(@PathVariable String empid,
			@PathVariable String taskname) {
		System.out.println("called");
		List<TaskTracker> task = service.searchbyEmpIDTaskName(empid, taskname);
		try {
			if (task == null)
				throw new CustomException("Employee :" + empid + " has no task by name " + taskname);
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			throw new CustomException("Employee :" + empid + " has no task by name " + taskname);
		}

		return new ResponseEntity<>(task, HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/fetch-by-empid-name-and-date/{empid}/{sdate}/{edate}/{taskname}")
	public ResponseEntity<List<TaskTracker>> searchbyEmpIdTaskNameDate(@PathVariable String empid,
			@PathVariable String sdate, @PathVariable String edate, @PathVariable String taskname) {

		Date convertedsDate = null;
		Date convertedeDate = null;
		try {
			convertedsDate = new SimpleDateFormat("yyyy-MM-dd").parse(sdate);
			convertedeDate = new SimpleDateFormat("yyyy-MM-dd").parse(edate);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}

		List<TaskTracker> task = service.searchbyEmpIdTaskNameDate(empid, taskname, convertedsDate, convertedeDate);
		try {
			if (task == null)
				throw new CustomException("Employee :" + empid + " has no task by name " + taskname
						+ " between dates : " + sdate + " and " + edate);
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			throw new CustomException("Employee :" + empid + " has no task by name " + taskname + " between dates : "
					+ sdate + " and " + edate);
		}

		return new ResponseEntity<>(task, HttpStatus.ACCEPTED);
	}

	@PutMapping(value = "/set-endtime")
	public void setEndTime() throws ParseException {
		service.setEndTime();
	}

	@PostMapping(path = "/fetch-bad-tasks-by-date/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<EmpTaskModel> getBadTask(@RequestBody Date date) {
		System.out.println(date);
		return service.getBadTask(date);
	}

	@GetMapping(value = "/fetch-bad-employees-by-date/{startdate}/{enddate}")
	public List<EmpTaskModel> getBadEmployeeWithDate(@PathVariable String startdate, @PathVariable String enddate) {
		Date convertedDates = null;
		Date convertedDatee = null;
		try {
			convertedDates = new SimpleDateFormat("yyyy-MM-dd").parse(startdate);
			convertedDatee = new SimpleDateFormat("yyyy-MM-dd").parse(enddate);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return service.badEmployeeAccToDate(convertedDates, convertedDatee);
	}

}
