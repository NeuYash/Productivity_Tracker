import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Employee } from '../model/employee';
import { TaskTracker } from '../model/tasktracker';
import { AUTH_API } from './loginservice.service';


@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  constructor(private http: HttpClient) {}

  fetchAssignedTasks(empId: String): Observable<TaskTracker[]> {
    return this.http.get<TaskTracker[]>(
      AUTH_API + 'employee/fetch-tasks/' + empId
    );
  }

  startNewTask(newTask: TaskTracker): Observable<TaskTracker> {
    return this.http.post<TaskTracker>(AUTH_API + 'tasktracker/', newTask);
  }

  fetchEmpById(empId: String): Observable<Employee> {
    return this.http.get<Employee>(
      AUTH_API + 'employee/fetch-employee/' + empId
    );
  }

  endTask(task: TaskTracker): Observable<TaskTracker> {
    return this.http.put<TaskTracker>(AUTH_API + 'tasktracker/', task);
  }

  deleteEmployee(empId: String): Observable<Employee> {
    return this.http.delete<Employee>(
      AUTH_API + 'employee/delete/' + empId
    );
  }

  updateAllEmployees(emplist:String[]){
    return this.http.put(
      AUTH_API + 'employee/update-isarchived/',emplist);
  }

  addEmployee(emp:Employee):Observable<Employee>{
    return this.http.post<Employee>(AUTH_API + 'employee/register', emp);
  }
}
