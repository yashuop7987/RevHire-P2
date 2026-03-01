import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { EmployerMenuComponent } from './components/employer-menu/employer-menu';
import { EmployerProfileComponent } from './components/employer-profile/employer-profile';
import { PostJobComponent } from './components/post-job/post-job';
import { ManageJobsComponent } from './components/manage-jobs/manage-jobs';
import { EditJobComponent } from './components/edit-job/edit-job';
import { JobseekerMenu } from './components/jobseeker-menu/jobseeker-menu';
import { SeekerDashboardComponent } from './components/seeker-dashboard/seeker-dashboard';
import { SeekerProfileComponent } from './components/seeker-profile/seeker-profile';
import { JobSearchComponent } from './components/job-search/job-search';
import { ResumeComponent } from './components/resume/resume';
import { Component } from '@angular/core';
import { JobDetailsComponent } from './components/job-details/job-details';
import { SavedJobsComponent } from './components/saved-jobs/saved-jobs';
import { ApplyJobComponent } from './components/apply-job/apply-job';
import { MyApplicationsComponent } from './components/my-applications/my-applications';

import { ApplicantDetailsComponent } from './components/applicant-details/applicant-details';
import { SeekerNotification } from './components/seeker-notification/seeker-notification';
import { ProcessApplicationsComponent } from './components/process-applications/process-applications';
export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'employer/dashboard', component: EmployerMenuComponent },
  { path: 'employer/profile', component: EmployerProfileComponent },
  {path: 'employer/create-job',component:PostJobComponent},
  {path:'employer/manage-jobs',component:ManageJobsComponent},
  { 
  path: 'employer/edit-job/:id', 
  component: EditJobComponent 
},
{path:'employer/view-applicants',component:ApplicantDetailsComponent},
{path:'employer/process-applications',component:ProcessApplicationsComponent},
{ path: 'jobseeker/menu', component:  JobseekerMenu,
    children: [
      // 1. This fills the white space by default
      
      
      // 2. This is the actual content for the white space
      { path: 'dashboard', component: SeekerDashboardComponent },
      {path:'profile', component: SeekerProfileComponent},
      {path:'search',component:JobSearchComponent},
      { path: 'job-details/:id', component: JobDetailsComponent },
      {path:'saved-jobs',component: SavedJobsComponent},
      {path:'my-applications',component:MyApplicationsComponent},
      
      {path:'resume',component:ResumeComponent},
      {path:'applyjob',component:ApplyJobComponent},
      {path:'notifications',component:SeekerNotification},
      
       { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
      
    ]
  },
  
  
  
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
