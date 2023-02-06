import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Admin } from '../model/admin';
import { Employee } from '../model/employee';
import { login } from '../model/login';
import { LoginserviceService } from '../service/loginservice.service';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})

export class LoginComponent implements OnInit {
  login: login;
  inputType = 'password';
  admin: Admin;
  roles: string[] = ['ADMIN', 'EMPLOYEE'];
  loginForm: FormGroup;
  hide:boolean=true;
  emp:Employee;
  constructor(
    private router: Router,
    private loginservice: LoginserviceService,
    public fb: FormBuilder,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.minLength]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      role: ['EMPLOYEE', [Validators.required, Validators.minLength]],
    });
    this.login = new login();
  }

  onSubmit() {
    console.log(this.loginForm.value);
    this.login.email = this.loginForm.value.email;
    this.login.password = this.loginForm.value.password;
    this.login.role = this.loginForm.value.role;
    this.loginservice.setEndTime().subscribe();
    if (this.login.role == 'ADMIN') {
      this.loginAdmin();
    }
    
    if (this.login.role == 'EMPLOYEE') {
      this.loginEmployee();
    }
  }
  toggle() {
    if (this.inputType == 'password'){ 
      this.inputType = 'text';
      this.hide = false;
  }
    else {
    this.inputType = 'password';
    this.hide = true;}
  }
  loginAdmin() {
    this.loginservice.checkAdminLogin(this.login).subscribe(
      (data) => {
        console.log(data);
        this.loginservice.saveAdmin(data);
        localStorage.setItem(data.email, data.adminId);
        this.router.navigate(['../admindashboard/']);
      },
      (err) => this.errorHandler(err)
    );
  }
  loginEmployee() {
    this.loginservice.checkEmpLogin(this.login).subscribe(
      (data:Employee) => {
        console.log(data);
        this.loginservice.saveEmployee(data);
        localStorage.setItem(data.email,data.empId);
        this.router.navigate(['../employeedashboard']);
      },
      (err) => this.errorHandler(err)
    );
  }

  

  errorHandler(err) {
    if (err.error.status != null)
      this.toastr.error('Error', 'Incorrect   Username or Password');
    else
      this.toastr.error('Error', 'Request Timed out, Please try again later');
  }
}
