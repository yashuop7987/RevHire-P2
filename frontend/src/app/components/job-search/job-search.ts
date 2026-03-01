import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import api from '../service/axios';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { RouterLink } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-job-search',
  standalone: true,
  imports: [CommonModule, FormsModule,RouterLink],
  templateUrl: './job-search.html',
  styleUrls: ['./job-search.css']
})
export class JobSearchComponent implements OnInit {
  jobs: any[] = [];
  isLoading = false;

  // Search Filters
  filters = {
    title: '',
    location: '',
    experience: '',
    jobType: '',
  };


  constructor(private cdr:ChangeDetectorRef){}


ngOnInit(): void {
  this.searchJobs();
  this.cdr.detectChanges();
}
  

 async searchJobs() {
  
  
  // Create a clean params object without empty strings
  const params: any = {};
  if (this.filters.title) params.title = this.filters.title;
  if (this.filters.location) params.location = this.filters.location;
  if (this.filters.experience) params.experience = this.filters.experience;
  if (this.filters.jobType) params.jobType = this.filters.jobType;
  try {
    const response = await api.get('/jobseeker/search', { params });
    this.jobs = response.data || []; 
    console.log('Search Result:', this.jobs);
    this.cdr.detectChanges();
  } catch (error: any) {
    console.error('Search failed', error);
    // Alert the user if it's a 403 or 500
    alert(error.response?.data?.message || 'Server error occurred');
  } finally {
  }
}

  async applyForJob(id: number) {
    try {
      await api.post(`/seeker/apply/${id}`);
      alert('Application submitted successfully!');
    } catch (error) {
      alert('You have already applied for this job or an error occurred.');
    }
  }
  async toggleSaveJob(job: any) {
  try {
    const profileRes = await api.get('/jobseeker/profile');
    const profileId = profileRes.data.id;

    if (job.isSaved) {
      await api.delete(`/jobseeker/unsave-job/${job.id}/${profileId}`);
      job.isSaved = false;
    } else {
      await api.post(`/jobseeker/save-job/${job.id}/${profileId}`);
      job.isSaved = true;
    }
    
    // Trigger UI refresh
    this.cdr.detectChanges();
  } catch (error) {
    console.error("Error toggling save status", error);
    alert("Could not update saved status.");
  }
}
  
}