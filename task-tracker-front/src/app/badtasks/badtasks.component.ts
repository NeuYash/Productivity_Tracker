import { Component, HostListener, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { DisplayModel } from '../model/model';
import { TaskService } from '../service/task.service';
import  jspdf from 'jspdf';    
import html2canvas from 'html2canvas';
import { downloadCSV } from '../model/downloadCSV';
import { AngularCsv } from 'angular7-csv/dist/Angular-csv';
import { Admin } from '../model/admin';
import { LoginserviceService } from '../service/loginservice.service';
@Component({
  selector: 'app-badtasks',
  templateUrl: './badtasks.component.html',
  styleUrls: ['./badtasks.component.css'],
})
export class BadtasksComponent implements OnInit {
  today: Date = new Date(Date.now());
  form: FormGroup;
  taskForm: FormGroup;
  resulteDate:Date;
  flag:boolean=false;
  yesterday: Date = new Date(); 
  display: DisplayModel;
  resultsDate: Date;
  displayObject: DisplayModel[] = new Array();
  csv:downloadCSV[]=[];
  adm:Admin;
  csvOptions = {
    fieldSeparator: ',',
    quoteStrings: '"',
    decimalseparator: '.',
    showLabels: true,
    showTitle: true,
    title: 'SuspiciousEmployeeStatus',
    useBom: true,
    noDownload: false,
    headers: ["EmpId","Name","TaskName","TaskDate","StartTime",'EndTime',"Duration","Description","Message","Activity"]
  };
  constructor(
    private router: Router,
    private taskService: TaskService,
    private toastr: ToastrService,
    private loginservice:LoginserviceService
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.getAdmin();
    this.populate();
  }

  getAdmin(){
    this.adm=this.loginservice.returnAdm();
  }
  populate() {
    this.yesterday.setDate(this.today.getDate() - 1);
    this.fetchBadTasksByDate(this.yesterday);
  }

  createForm() {
    this.form = new FormGroup({
      taskDate: new FormControl(),
    });

    this.taskForm = new FormGroup({
      startDate: new FormControl(),
      endDate: new FormControl(),
    });
  }

  errorHandler(err) {
    if (err.error.message != null)
      this.toastr.error('Error', err.error.message);
    else
      this.toastr.error('Error', 'Request Timed out, Please try again later');
  }

  fetchBadTasksByDate(date: Date) {
    this.taskService.fetchBadTasksByDate(date).subscribe(
      (data) => {
        this.displayObject = data;
        this.resultsDate = date;
        console.log(data);
      },
      (err) => this.errorHandler(err)
    );
  }

  fetchBadEmployeeByDate(startDate:Date,endDate:Date){
    if(startDate==null)
    {
      this.toastr.error("Start date is missing");
    }
    else if(endDate==null)
    {
      this.toastr.error("End date is missing");
    }
    else{
    this.taskService.fetchBademployeeByDate(startDate,endDate).subscribe(
      (data)=>{
        this.displayObject=data;
        console.log(data);
      },
      // (err)=>this.errorHandler(err)
    );
  }
  }

  onSubmit() {
    let date = new Date(this.form.value.taskDate);
    console.log(date);
    this.fetchBadTasksByDate(date);
  }

  onSubmit1(){
    if(this.taskForm.value.startDate && this.taskForm.value.endDate)
      this.flag=true;
    else
      this.flag=false;
    this.resultsDate = this.taskForm.value.startDate;
    this.resulteDate = this.taskForm.value.endDate;
    this.fetchBadEmployeeByDate(this.taskForm.value.startDate,this.taskForm.value.endDate);
  }

  downloadCSV(){
    var k=0
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
            var endtime=new Date(v.tasks[j].endTime);
            this.csv[k].duration=this.transform(v.tasks[j].duration);
            this.csv[k].additionalDetails=v.tasks[j].additionalDetails;
            this.csv[k].message=this.displayObject[i].message;
            if((v.tasks[j].taskName=="Break" &&v.tasks[j].duration>3600000)||(v.tasks[j].duration>10800000)||
            (endtime.getHours()==20 && endtime.getMinutes()==1)){
            this.csv[k].activity="Inactive";}
            else 
            this.csv[k].activity="Active";
            k++;
        }
      }
      new  AngularCsv(this.csv, "EmployeeStatus", this.csvOptions);
      this.toastr.success("Excel file Downloaded","File Downloaded");
	  this.csv=[];
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
    this.toastr.success("File Downloaded Successfully","Download Successful");
    
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

  back(){
    this.router.navigate(['./admindashboard']);
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
