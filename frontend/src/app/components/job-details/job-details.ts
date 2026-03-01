import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import api from '../service/axios';
import { ApplyJobComponent } from '../apply-job/apply-job';
import { ChangeDetectorRef } from '@angular/core'; // Import the new component

@Component({
  selector: 'app-job-details',
  standalone: true,
  // Add ApplyJobComponent to imports
  imports: [CommonModule, RouterModule, ApplyJobComponent], 
  templateUrl: './job-details.html'
})
export class JobDetailsComponent implements OnInit ,OnDestroy{
  job: any = null;
  isLoading = true;
  isSaved: boolean = false;
  profileId: number = 0;
  isApplying = false; // Toggle for the apply section

  constructor(private route: ActivatedRoute,private cdr:ChangeDetectorRef) {}

  ngOnInit(): void {
    const jobId = this.route.snapshot.paramMap.get('id');
    if (jobId) {
      this.fetchJobDetails(jobId);
     
    }
    
  }

  async fetchJobDetails(id: string) {
    this.isLoading = true;
    try {
      // 1. Get Profile first to ensure seekerId is ready
     const profileRes = await api.get('/jobseeker/profile');
    this.profileId = profileRes.data.id;
     
    
    // 2. Fetch Job and Saved Status in parallel for speed
    const [jobRes, savedRes] = await Promise.all([
      api.get(`/jobseeker/job/${id}`),
      api.get(`/jobseeker/is-saved/${id}/${this.profileId}`),
       
      
      
    ]);
    

    this.job = jobRes.data;
    this.isSaved = savedRes.data;
    
    
    } catch (error: any) {
      console.error('Error:', error);
    } finally {
      this.isLoading = false; 
      this.cdr.detectChanges();
    }
  }

  // Changed to a toggle so the Apply component shows up
  toggleApply() {
    this.isApplying = !this.isApplying;
  }

  async toggleSaveJob() {
    try {
      if (this.isSaved) {
        await api.delete(`/jobseeker/unsave-job/${this.job.id}/${this.profileId}`);
        this.isSaved = false;
      } else {
        await api.post(`/jobseeker/save-job/${this.job.id}/${this.profileId}`);
        this.isSaved = true;
      }
      this.cdr.detectChanges();
    } catch (error) {
      alert("Could not update saved status.");
    }
  }
}