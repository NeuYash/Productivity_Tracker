import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskTracker } from '../model/tasktracker';
import { DisplayModel } from '../model/model';
import { DatePipe } from '@angular/common';
import { AUTH_API } from './loginservice.service';
@Injectable({
  providedIn: 'root',
})
export class TaskService {
  constructor(private http: HttpClient,public datepipe:DatePipe) {}

  fetchAssignedTasks(empId: String): Observable<TaskTracker[]> {
    return this.http.get<TaskTracker[]>(
      AUTH_API + 'employee/fetch-tasks/' + empId
    );
  }
/////////////////////////////////////////////////////////////
  fetchTasksByDate(sdate: Date,edate: Date): Observable<TaskTracker[]> {
    return this.http.get<TaskTracker[]>(
      AUTH_API + 'tasktracker/fetch-tasks-by-date/'+sdate+'/'+edate);
  }

  /////////////////////////////////////////////////////////////
  fetchTasksByEmpIdAndDate(
    empId: string,
    sdate: Date,
    edate: Date
  ): Observable<TaskTracker[]>{
    return this.http.get<TaskTracker[]>(
      AUTH_API + 'tasktracker/search-emp-tasks-by-date/' + empId+'/'+sdate+'/'+edate);
  }
  //////////////////////////////////////////////////////////////

  fetchByTaskName(taskName: String): Observable<TaskTracker[]> {
    return this.http.get<TaskTracker[]>(
      AUTH_API + 'tasktracker/fetch-tasks-by-name/' + taskName
    );
  }
  /////////////////////////////////////////////////////////////////////////
  fetchTasksByDateAndName(
    sdate: Date,
    edate: Date,
    taskName: string
  ): Observable<TaskTracker[]> {
    return this.http.get<TaskTracker[]>(
      AUTH_API + 'tasktracker/fetch-by-name-and-date/' + taskName + '/' + sdate + '/' + edate
    );
  }
  /////////////////////////////////////////////////////////////////////////

  fetchTasksByEmpIdAndName(
    empId: string,
    taskName: string
  ): Observable<TaskTracker[]> {
    return this.http.get<TaskTracker[]>(
      AUTH_API + 'tasktracker/fetch-by-empid-and-name/' + empId + '/' + taskName
    );
  }
  ///////////////////////////////////////////////////////////////////////////
  fetchTasksByEmpIdDateAndName(
    empId: string,
    sdate: Date,
    edate:Date,
    taskName: string
  ): Observable<TaskTracker[]> {
    return this.http.get<TaskTracker[]>(
      AUTH_API +
        'tasktracker/fetch-by-empid-name-and-date/' +
        empId + '/' + sdate + '/' + edate + '/' + taskName
    );
  }
  //////////////////////////////////////////////////////////////////////////////

  fetchBadTasksByDate(date: Date): Observable<DisplayModel[]> {
    console.log(date);
    return this.http.post<DisplayModel[]>(
      AUTH_API + 'tasktracker/fetch-bad-tasks-by-date/',
      date
    );
  }

  downloadReport(disp:DisplayModel[]){
    return this.http.post(
      AUTH_API+ 'tasktracker/downloadcsv',disp
    );
  }

  fetchBademployeeByDate(startdate: Date,enddate : Date): Observable<DisplayModel[]> {
    let startdate1=this.datepipe.transform(startdate,'yyyy-MM-dd');
    let enddate1=this.datepipe.transform(enddate,'yyyy-MM-dd');
    console.log(startdate,enddate);
    return this.http.get<DisplayModel[]>(
      AUTH_API + 'tasktracker/fetch-bad-employees-by-date/'+startdate1+'/'+enddate1);
   }

  
}
