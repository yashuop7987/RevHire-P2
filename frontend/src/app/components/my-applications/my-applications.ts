import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import api from '../service/axios';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-my-applications',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-applications.html'
})
export class MyApplicationsComponent implements OnInit {
  applications: any[] = [];
  isLoading: boolean = true;
  seekerId: number = 0;

  constructor(private cdr:ChangeDetectorRef){}

  async ngOnInit() {
    await this.loadApplications();
    this.cdr.detectChanges();
  }
 
  async loadApplications() {
    this.isLoading = true;
    try {
      // 1. First, get James's profile to get his seekerId
      const profileRes = await api.get('/jobseeker/profile');
      this.seekerId = profileRes.data.id;

      // 2. Fetch all applications for this seeker
      const response = await api.get(`/jobseeker/my-applications/${this.seekerId}`);
      this.applications = response.data;
    } catch (error) {
      console.error("Failed to load applications:", error);
    } finally {
      this.isLoading = false;
    }
  }

  // Helper function to color-code status badges
  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDING': return 'bg-warning text-dark';
      case 'ACCEPTED': return 'bg-success';
      case 'REJECTED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }
  async withdrawApplication(appId: number) {
  if (confirm("Are you sure you want to withdraw this application?")) {
    try {
      await api.delete(`/jobseeker/withdraw-application/${appId}`);
      alert("Application withdrawn.");
      
      // Refresh the list locally to show it's gone
      this.applications = this.applications.filter(app => app.id !== appId);
    } catch (error) {
      console.error("Withdrawal failed", error);
      alert("Failed to withdraw application.");
    }
  }
}
}