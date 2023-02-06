import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { LoginserviceService } from './service/loginservice.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'yp-tracker';
  constructor(private router:Router,private loginservice:LoginserviceService){}  
 
  // logout() {
  //   console.log("logged out of application")
  //  if(localStorage.getItem('empId')!=null){
  //   this.loginservice.logout(localStorage.getItem('empId')).subscribe(
  //     (data)=>{console.log(data)},
  //     );
  //  }
  //   this.router.navigate(['./login']);
  //   localStorage.removeItem('empId');
  //   localStorage.removeItem('adminId');
  // } 

  // @HostListener('window:beforeunload', ['$event'])
  // beforeunloadHandler(event) {
  //   console.log(event);
  //   return false;
  // }
  
  // @HostListener('window:unload', ['$event'])
  // unloadHandler(event) {
  //     console.log("Leaving site, window:unload")
  //     this.logout();
  // }
}
