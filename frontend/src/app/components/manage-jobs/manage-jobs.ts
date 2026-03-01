import { Component, OnInit,ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import api from '../service/axios';

@Component({
  selector: 'app-manage-jobs',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './manage-jobs.html',
  styleUrls: ['./manage-jobs.css']
})
export class ManageJobsComponent implements OnInit {
  jobs: any[] = [];
  isLoading: boolean = true;

  private router: any;
  constructor(
    private cdr: ChangeDetectorRef // 2. Inject it here
  ) {} // Add this line to declare the router property

  ngOnInit(): void {
    const token = localStorage.getItem('token');
  if (token) {
    this.fetchEmployerJobs();
  } else {
    console.error("No token found, redirecting to login...");
    this.router.navigate(['/login']);
  }
  }

  
  async fetchEmployerJobs() {
    try {
      // Assuming your backend has an endpoint for employer-specific jobs
      const response = await api.get('/employer/jobs');
      this.jobs = [...response.data];
      this.cdr.detectChanges();
      
    } catch (error) {
      console.error('Error fetching jobs:', error);
      this.isLoading = false;
    }
    finally{
      this.isLoading = false;
    }
  }

  async deleteJob(jobId: number) {
    if (confirm('Are you sure you want to delete this job posting?')) {
      try {
        await api.delete(`/employer/jobs/${jobId}`);
        this.jobs = this.jobs.filter(j => j.id !== jobId);
        alert('Job deleted successfully');
      } catch (error) {
        alert('Failed to delete job');
      }
    }
  }

  // manage-jobs.component.ts

async toggleJobStatus(job: any) {
  // Flip the current boolean value
  const newActiveStatus = !job.active; 
  
  try {
    // Ensure your backend @PutMapping handles the 'active' boolean
    await api.put(`/employer/jobs/${job.id}/status`, { active: newActiveStatus });
    
    // Update local UI state
    
    job.active = newActiveStatus;
    this.cdr.detectChanges();
    console.log(`Job ${job.id} is now ${job.active ? 'Active' : 'Inactive'}`);
  } catch (error) {
    console.error('Failed to update status:', error);
    alert('Failed to update job status');
  }
}
}