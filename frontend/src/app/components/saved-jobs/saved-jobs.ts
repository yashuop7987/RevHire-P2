import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import api from '../service/axios';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-saved-jobs',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './saved-jobs.html'
})
export class SavedJobsComponent implements OnInit {
  savedJobs: any[] = [];
  isLoading = true;
  profileId: number = 0;


  constructor(private cdr:ChangeDetectorRef){}


  async ngOnInit() {
    await this.loadProfileAndSavedJobs();
    this.cdr.detectChanges();
  }

  async loadProfileAndSavedJobs() {
    try {
      const profileRes = await api.get('/jobseeker/profile');
      this.profileId = profileRes.data.id;
      
      const res = await api.get(`/jobseeker/my-saved-jobs/${this.profileId}`);
      this.savedJobs = res.data;
      this.cdr.detectChanges();
    } catch (error) {
      console.error("Error loading saved jobs", error);
    } finally {
      this.isLoading = false;
    }
  }

  async unsaveJob(jobId: number) {
    try {
      await api.delete(`/jobseeker/unsave-job/${jobId}/${this.profileId}`);
      this.savedJobs = this.savedJobs.filter(j => j.id !== jobId);
    } catch (error) {
      alert("Error removing job");
    }
  }
}