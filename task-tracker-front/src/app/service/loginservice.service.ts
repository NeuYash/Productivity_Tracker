import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskTracker } from '../model/tasktracker';
import { Employee } from '../model/employee';
import { Admin } from '../model/admin';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

//export const AUTH_API = 'http://localhost:8293/'; 
export const AUTH_API = 'http://10.252.159.80:8081/demo-0.0.1-SNAPSHOT/';
@Injectable({
  providedIn: 'root',
})
export class LoginserviceService {
  constructor(private http: HttpClient) {}
  employee:Employee;
  admin:Admin;
  checkEmpLogin(login) {
    return this.http.post(AUTH_API + 'employee/login', login, httpOptions);
  }

  saveEmployee(e:Employee){
    this.employee=e;
  }

  saveAdmin(a:Admin){
    this.admin=a;
  }

  returnEmp():Employee{
    return this.employee;
  }

  returnAdm():Admin{
    return this.admin;
  }

  checkAdminLogin(login): Observable<any> {
    return this.http.post(AUTH_API + 'admin/login', login, httpOptions);
  }

  setEndTime(): Observable<any> {
    return this.http.put(
      AUTH_API+'tasktracker/set-endtime',
      httpOptions
    );
  }
  logout(empid:string):Observable<TaskTracker[]>{
    return this.http.put<any>( AUTH_API+'tasktracker/logout/'+empid,httpOptions);
  }
  loggedInEmp() {
    return !!localStorage.getItem(this.employee.email);
  }
  loggedInAdmin() {
  return !!localStorage.getItem(this.admin.email);
  }
}
