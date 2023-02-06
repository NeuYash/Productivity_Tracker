import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TaskTracker } from '../model/tasktracker';
import { EmployeeService } from '../service/employee.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Employee } from '../model/employee';
import { ToastrService } from 'ngx-toastr';
import { TaskService } from '../service/task.service';
import { LoginserviceService } from '../service/loginservice.service';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-employeedashboard',
  templateUrl: './employeedashboard.component.html',
  styleUrls: ['./employeedashboard.component.css'],
})
export class EmployeedashboardComponent implements OnInit {
  storedTasks: TaskTracker[];
  model: any = {};
  form: FormGroup;
  newTask: TaskTracker = new TaskTracker();
  today: number = Date.now();

  tasks: string[] = [
    'Online Training',
    'RMG initiative Training',
    'Meeting',
    'Interview',
    'Break',
    'Other',
  ];
  empId: string;//= String(localStorage.getItem('empId'));
  employee: Employee = new Employee();
  e:Employee;

  constructor(
    private router: Router,
    private employeeService: EmployeeService,
    private taskService: TaskService,
    private loginservice:LoginserviceService,
    private toastr: ToastrService
    //private loginclass:LoginComponent
  ) {}

  ngOnInit() {
    this.createForm();
    this.getEmployee();
    this.reloadData();
  }

  get f() {
    return this.form.controls;
  }

  createForm() {
    this.form = new FormGroup({
      taskName: new FormControl('', [Validators.required]),
      additionalDetails: new FormControl('', [
        Validators.required,
        Validators.minLength(10),
      ]),
    });
  }

  getEmployee(){
   this.e=this.loginservice.returnEmp();
   console.log("EmployeeClass Object",this.e);
   this.empId=this.e.empId;
  }

  reloadData() {
    this.taskService.fetchTasksByEmpIdAndDate(this.empId, new Date(),new Date()).subscribe(
      (data) => {
        this.storedTasks = data;
        console.log(data);
      },
      (err) => {
        console.log(err);
      },
      () => {
        this.fetchEmployee();
      }
    );
  }
  errorHandler(err) {
    if (err.error.message != null)
      this.toastr.error('Error', err.error.message);
    else
      this.toastr.error('Error', 'Request Timed out, Please try again later');
  }
  fetchEmployee() {
    this.employeeService.fetchEmpById(this.empId).subscribe(
      (data) => (this.employee = data),
      (err) => this.errorHandler(err)
    );
  }

  onSubmit() {
    this.newTask = Object.assign(this.newTask, this.form.value);
    this.newTask.employee = this.employee;
    this.employeeService.startNewTask(this.newTask).subscribe(
      (data) => {
        console.log(data);
        this.toastr.success('Task added Successfully');
      },
      (err) => this.errorHandler(err),
      () => this.reloadData()
    );
  }

  endTask(task: TaskTracker) {
    this.employeeService.endTask(task).subscribe(
      (data) =>
        this.toastr.success(
          "Great Job, You've successfully completed the task."
        ),
      (err) => this.errorHandler(err),
      () => this.reloadData()
    );
  }

  logoutEmp(){
    console.log("Employee Logged Out");
    this.loginservice.logout(this.empId).subscribe(
      (data)=>{console.log(data)},
      );
      this.router.navigate(['./login']);
      localStorage.removeItem(this.e.email);
  }
  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    console.log(event);
    return false;
  }
  
  @HostListener('window:unload', ['$event'])
  unloadHandler(event) {
      console.log("Leaving site, window:unload")
      this.logoutEmp();
  }
}
