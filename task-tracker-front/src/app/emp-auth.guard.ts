import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { LoginserviceService } from './service/loginservice.service';
@Injectable({
  providedIn: 'root'
})
export class EmpAuthGuard implements CanActivate {
  constructor(
    private loginservice: LoginserviceService,
    private router: Router
  ) {}
  canActivate():boolean {
    if (this.loginservice.loggedInEmp()) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
  
}
