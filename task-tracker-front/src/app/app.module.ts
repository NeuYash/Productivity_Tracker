import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AdmindashboardComponent } from './admindashboard/admindashboard.component';
import { EmployeedashboardComponent } from './employeedashboard/employeedashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { DurationPipe } from './duration.pipe';
import { AuthGuard } from './service/auth.guard';
import { BadtasksComponent } from './badtasks/badtasks.component';
import { DatePipe } from '@angular/common';
import { EmpAuthGuard } from './emp-auth.guard';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdmindashboardComponent,
    EmployeedashboardComponent,
    DurationPipe,
    BadtasksComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ToastrModule.forRoot(),
    BrowserAnimationsModule,
  ],
  providers: [AuthGuard,DatePipe,EmpAuthGuard],
  bootstrap: [AppComponent],
})
export class AppModule {}
