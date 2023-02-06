import { Component, HostListener, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { DisplayModel } from '../model/model';
import { TaskTracker } from '../model/tasktracker';
import { EmployeeService } from '../service/employee.service';
import { TaskService } from '../service/task.service';
import  jspdf from 'jspdf';    
import html2canvas from 'html2canvas'; 
import * as XLSX from 'xlsx';
import { AngularCsv } from 'angular7-csv/dist/Angular-csv'
import { Employee } from '../model/employee';
import { DatePipe, formatDate } from '@angular/common';
import { downloadCSV } from '../model/downloadCSV';
import { Admin } from '../model/admin';
import { LoginserviceService } from '../service/loginservice.service';

@Component({
  selector: 'app-admindashboard',
  templateUrl: './admindashboard.component.html',
  styleUrls: ['./admindashboard.component.css'],
})



export class AdmindashboardComponent implements OnInit {
  /////////////////////////
  csvOptions = {
    fieldSeparator: ',',
    quoteStrings: '"',
    decimalseparator: '.',
    showLabels: true,
    showTitle: true,
    title: 'EmployeeStatus',
    useBom: true,
    noDownload: false,
    headers: ["EmpId","Name","TaskName","TaskDate","StartTime",'EndTime',"Duration","Description","Message","Activity"]
  };
  budate :Date;
  csv:downloadCSV[]=[];
  ////////////////////////
  taskNames: string[] = [
    'Online Training',
    'RMG initiative Training',
    'Meeting',
    'Interview',
    'Break',
    'Other',
  ];
  employeeAction:[][];

  flag:boolean=false;
  fetchedTasks: TaskTracker[];
  form: FormGroup;
  today: Date = new Date(Date.now());
  yesterday: Date = new Date();
  resultsDate: Date = this.yesterday;
  resultSDate: Date= this.yesterday;
  resultEDate: Date= this.yesterday;
  display: DisplayModel;
  displayObject: DisplayModel[] = new Array();
  s:String;
  formObject = {
    startDate: new Date(),
    endDate: new Date(),
    taskName: '',
    empId: '',
  };

  data:any[]
  emp:Employee;
  adminId: string; //= String(localStorage.getItem('adminId'));
  adm:Admin;
  constructor(
    private router: Router,
    private taskService: TaskService,
    private toastr: ToastrService,
    private employeeService:EmployeeService,
    private loginservice:LoginserviceService,
    public datepipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.getAdmin();
    this.populate();
    
  }

  getAdmin(){
    this.adm=this.loginservice.returnAdm();
    this.adminId=this.adm.adminId;
  }

  sortFetchedTasks() {
    let length = 0;
    let firstFlag = 0;
    this.displayObject = [];
    console.log(this.displayObject.length);
    this.fetchedTasks.map((task) => {
      let flag = 0;

      for (let i = 0; i <= length; i++) {
        if (this.displayObject.length === 0) {
          this.displayObject.push(this.newDisplayObject(task));
          flag = 1;
        } else if (length !== 0 || firstFlag === 1) {
          if (task.employee.empId === this.displayObject[i].empId) {
            this.displayObject[i].totalDuration += task.duration;
            this.displayObject[i].tasks.push(task);
            flag = 1;
          }
        }
      }
      if (flag === 0) {
        this.displayObject.push(this.newDisplayObject(task));
        length = length + 1;
        console.log('hii ', length);
      }
      if (this.displayObject.length === 1) {
        firstFlag = 1;
      }
      flag = 0;
    });

    console.log(this.displayObject);
  }

  newDisplayObject(task: TaskTracker) {
    let display = new DisplayModel();
    display.empId = task.employee.empId;
    display.name = task.employee.name;
    display.totalDuration += task.duration;
    display.tasks.push(task);

    return display;
  }
  get f() {
    return this.form.controls;
  }

  errorHandler(err) {
    if (err.error.message != null)
      this.toastr.error('Error', err.error.message);
    else
      this.toastr.error('Error', 'Request Timed out, Please try again later');
  }

  populate() {
    this.yesterday.setDate(this.today.getDate() - 1);
    console.log(this.yesterday);
    this.fetchTasksByDate(this.yesterday,this.yesterday);
  }

  createForm() {
    this.form = new FormGroup({
      startDate: new FormControl(''),
      endDate: new FormControl(''),
      taskName: new FormControl(''),
      empId: new FormControl(''),
    });
  }
  onSubmit() {
    Object.assign(this.formObject, this.form.value);
    console.log(this.formObject.startDate);
    this.flag=false;

    //start date is not null, rest are null
    if ( 
      this.formObject.startDate &&
      this.formObject.endDate &&
      this.formObject.empId === '' &&
      this.formObject.taskName === ''
    ) {
      this.flag=true;
      //this.fetchTasksByDate(new Date(this.formObject.startDate));
      this.fetchTasksByDate(new Date(this.formObject.startDate),new Date(this.formObject.endDate));
    }

    //startdate and employee id is not null
    else if (
      this.formObject.startDate &&
      this.formObject.endDate &&
      this.formObject.empId != '' &&
      this.formObject.taskName === ''
    )
      this.fetchTasksByEmpIdAndDate(
        this.formObject.empId,
        new Date(this.formObject.startDate),new Date(this.formObject.endDate)
      );
    //only employee id is present
    else if (
      !this.formObject.startDate &&
      !this.formObject.endDate &&
      this.formObject.empId != '' &&
      this.formObject.taskName === ''
    )
      this.fetchTasksByEmpId(this.formObject.empId);
    //only taskName is not null
    else if (
      !this.formObject.startDate &&
      !this.formObject.endDate &&
      this.formObject.empId === '' &&
      this.formObject.taskName != ''
    )
      this.fetchTasksByTaskName(this.formObject.taskName);
    //start date and taskname are not null
    else if (
      this.formObject.startDate &&
      this.formObject.endDate &&
      this.formObject.empId === '' &&
      this.formObject.taskName != ''
    )
      this.fetchTasksByDateAndName(
        this.formObject.startDate,
        this.formObject.endDate,
        this.formObject.taskName
      );
    //only startdate is null
    else if (
      !this.formObject.startDate &&
      !this.formObject.endDate &&
      this.formObject.empId != '' &&
      this.formObject.taskName != ''
    )
      this.fetchTasksByEmpIdAndName(
        this.formObject.empId,
        this.formObject.taskName
      );
    //All fields are present
    else if (
      this.formObject.startDate &&
      this.formObject.endDate &&
      this.formObject.empId != '' &&
      this.formObject.taskName != ''
    )
      this.fetchTasksByEmpIdDateAndName(
        this.formObject.empId,
        this.formObject.startDate,
        this.formObject.endDate,
        this.formObject.taskName
      );
    else this.toastr.info('Oops!', 'Search bar is empty');

   this.createForm();  //will set initial values to form once function is called 
  }

  // These functions are getting used for service calls in the backend

  ///////////////////////////////////////////////////////////////////////////
  fetchTasksByDate(sdate: Date,edate: Date) {
    this.taskService.fetchTasksByDate(sdate,edate).subscribe(
      (data) => {
        this.fetchedTasks = data;
        this.resultsDate = sdate;
        this.resultSDate=sdate;
        this.resultEDate=edate;
        this.sortFetchedTasks();
        console.log(sdate,edate);
      },
      (err) => this.errorHandler(err)
    );
  }
  /////////////////////////////////////////////////////////////////////////
 fetchTasksByEmpIdAndDate(empId: string, sdate: Date,edate:Date) {
    console.log(sdate,edate, empId);
    this.taskService.fetchTasksByEmpIdAndDate(empId, sdate,edate).subscribe(
      (data) => {
        this.fetchedTasks = data;
        this.resultsDate = sdate;
        this.resultSDate=sdate;
        this.resultEDate=edate;
        this.sortFetchedTasks();
      },
      (err) => this.errorHandler(err)
    );
  }
///////////////////////////////////////////////////////////////////////////
  fetchTasksByEmpId(empId: string) {
    this.taskService.fetchAssignedTasks(empId).subscribe(
      (data) => {
        this.fetchedTasks = data;
        this.sortFetchedTasks();
        this.resultSDate=null;
        this.resultEDate=null;
      },
      (err) => {
        console.log(err);
      }
    );
  }

  fetchTasksByTaskName(taskName: string) {
    this.taskService.fetchByTaskName(taskName).subscribe(
      (data) => {
        console.log(data);
        this.fetchedTasks = data;
        this.sortFetchedTasks();
        this.resultSDate=null;
        this.resultEDate=null;
      },
      (err) => {
        console.log(err);
      }
    );
  }
 /////////////////////////////////////////////////////////////////////////////////
  fetchTasksByDateAndName(sDate: Date,eDate:Date, taskName: string) {
    this.taskService.fetchTasksByDateAndName(sDate,eDate, taskName).subscribe(
      (data) => {
        this.fetchedTasks = data;
        this.resultsDate = sDate;
        this.resultSDate=sDate;
        this.resultEDate=eDate;
        this.sortFetchedTasks();
      },
      (err) => this.errorHandler(err)
    );
  } 
  /////////////////////////////////////////////////////////////////////////////////////

  fetchTasksByEmpIdAndName(empId: string, taskName: string) {
    this.taskService.fetchTasksByEmpIdAndName(empId, taskName).subscribe(
      (data) => {
        this.fetchedTasks = data;
        this.sortFetchedTasks();
        this.resultSDate=null;
        this.resultEDate=null;
      },
      (err) => this.errorHandler(err)
    );
  }
 /////////////////////////////////////////////////////////////////////////
  fetchTasksByEmpIdDateAndName(
    empId: string,
    sdate: Date,
    edate: Date,
    taskName: string
  ) {
    this.taskService
      .fetchTasksByEmpIdDateAndName(empId, sdate,edate, taskName)
      .subscribe(
        (data) => {
          this.fetchedTasks = data;
          this.resultsDate = sdate;
          this.resultSDate=sdate;
          this.resultEDate=edate;
          this.sortFetchedTasks();
        },
        (err) => this.errorHandler(err)
      );
  }
 ////////////////////////////////////////////////////////////////////////////
  downloadCSV(){
    console.log("Download CSV called");
    var k=0;
    for(var i=0;i<this.displayObject.length;i++){
      const v=this.displayObject[i];
      for(var j=0;j<v.tasks.length;j++){
        this.csv[k]=new downloadCSV();
        this.csv[k].empId=this.displayObject[i].empId;
        this.csv[k].name=this.displayObject[i].name;
        this.csv[k].taskName=v.tasks[j].taskName;
        this.csv[k].taskDate=v.tasks[j].taskDate;
        this.csv[k].startTime=new DatePipe("en-US").transform(v.tasks[j].startTime, 'h:mm a');
        this.csv[k].endTime=new DatePipe("en-US").transform(v.tasks[j].endTime, 'h:mm a');
        this.csv[k].duration= this.transform(v.tasks[j].duration);
        this.csv[k].additionalDetails=v.tasks[j].additionalDetails;
        var endtime=new Date(v.tasks[j].endTime);
        if(v.tasks[j].duration>14400000)
        this.csv[k].message="Task duration more than 4 hours";
        else if(v.tasks[j].taskName=="Break" && v.tasks[j].duration>3600000)
        this.csv[k].message="Break more than 1 hours";
        else if(endtime.getHours()==20 && endtime.getMinutes()==1)
        this.csv[k].message="Task running after office hours";
        else 
        this.csv[k].message="Valid Task";
        if((v.tasks[j].taskName=="Break" &&v.tasks[j].duration>3600000)||(v.tasks[j].duration>10800000)
        ||(endtime.getHours()==20 && endtime.getMinutes()==1)){
        this.csv[k].activity="Inactive";}
        else 
        this.csv[k].activity="Active";
        k++;
    }
  }
  new  AngularCsv(this.csv, "EmployeeStatus", this.csvOptions);
  this.toastr.success("Csv file Downloaded","File Downloaded");
  this.csv=[];
}
  
      onFileChange(evt: any) {
        const target : DataTransfer =  <DataTransfer>(evt.target);
        
        if (target.files.length !== 1) throw new Error('Cannot use multiple files');
    
        const reader: FileReader = new FileReader();
    
        reader.onload = (e: any) => {
          const bstr: string = e.target.result;
    
          const wb: XLSX.WorkBook = XLSX.read(bstr, { type: 'binary' , cellDates:true});
    
          const wsname : string = wb.SheetNames[0];
    
          const ws: XLSX.WorkSheet = wb.Sheets[wsname];
    
          console.log(ws);
    
          this.employeeAction = (XLSX.utils.sheet_to_json(ws, { header: 1 }));
    
         this.EmployeeAction();
       };
        reader.readAsBinaryString(target.files[0]);
     }

 EmployeeAction(){
       console.log("EmployeeAction function");
       let emplist: string[]=[];
       for(var i=1;i<this.employeeAction.length;i++){
           const data1:any[]=this.employeeAction[i];
           this.emp=new Employee();
           this.emp.empId=data1[0].toString();
           this.emp.name=data1[1];
           this.emp.email=data1[3];
           this.emp.password=data1[2]; 
           this.emp.role="EMPLOYEE";
          emplist[i-1]=data1[0].toString();
           this.employeeService.addEmployee(this.emp).subscribe(
              (r)=>{
                console.log(r);
              },
              (err) => this.errorHandler(err)
            );
           this.toastr.success("Employees Updated Successfully","Update Successful");
       }
       this.employeeService.updateAllEmployees(emplist).subscribe();
     }

  public captureScreen()  
  {  
    var data = document.getElementById('contentToConvert');  
    html2canvas(data).then(canvas => {  
      // Few necessary setting options  
      var imgWidth = 208;   
      var pageHeight = 295;    
      var imgHeight = canvas.height * imgWidth / canvas.width;  
      // var imgHeight=400;
      var heightLeft = imgHeight;  
  
      const contentDataURL = canvas.toDataURL('image/png')  
      let pdf = new jspdf('p', 'mm', 'a4'); // A4 size page of PDF  
      var position = 0;  
      pdf.addImage(contentDataURL, 'PNG', 0, position, imgWidth, imgHeight)  
      pdf.save('employeeStatus.pdf'); // Generated PDF   
    }); 
    this.toastr.success("PDF Downloaded Successfully","Download Successful");
  } 

  status(endtime:Date):boolean{
    var date=new Date(endtime);
   if(date.getHours()==20 && date.getMinutes()==1)
    return true;
    else 
    return false;
 }

 transform(value: number, ...args: unknown[]): string {
  if (value < 0) return value + '';
  if (value == undefined) return 'No Value';

  let totalSec = Math.floor(value / 1000);
  let hours = Math.floor(totalSec / 3600);
  totalSec = totalSec - hours * 3600;
  let min = Math.floor(totalSec / 60);
  let sec = totalSec - min * 60;
  return `${hours}hr ${min} min ${sec} sec`;
}

  badActivity(){
    this.router.navigate(['./badtasks']);
  }

  logoutAdm(){
    console.log("Logged Out of Admin");
    this.router.navigate(['./login']);
    localStorage.removeItem(this.adm.email);
  }

  @HostListener('window:beforeunload', ['$event'])
  beforeunloadHandler(event) {
    console.log(event);
    return false;
  }
  
  @HostListener('window:unload', ['$event'])
  unloadHandler(event) {
      console.log("Leaving site, window:unload")
      this.logoutAdm();
  }
}
