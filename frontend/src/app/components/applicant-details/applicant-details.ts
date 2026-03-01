import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import api from '../service/axios';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-applicant-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './applicant-details.html'
})
export class ApplicantDetailsComponent implements OnInit {
  applicants: any[] = [];
  isLoading: boolean = true;
  employerId: number = 0;

constructor(private cdr:ChangeDetectorRef){}

  async ngOnInit() {
    await this.fetchApplicants();
    this.cdr.detectChanges();
  }

  async fetchApplicants() {
    this.isLoading = true;
    try {
      // 1. First, get the logged-in employer's profile info
      const profileRes = await api.get('/employer/profile');
      this.employerId = profileRes.data.id;

      // 2. Use the employerId to fetch their specific applicants
      const response = await api.get(`/employer/applicants/${this.employerId}`);
      this.applicants = response.data;
    } catch (error) {
      console.error("Error fetching applicants:", error);
    } finally {
      this.isLoading = false;
    }
  }

  async updateStatus(applicationId: number, status: string) {
    try {
      // Sending status as a query parameter to match the @RequestParam in Spring Boot
      await api.put(`/employer/application/${applicationId}/status`, null, {
        params: { status: status }
      });
      alert(`Application marked as ${status}`);
      await this.fetchApplicants(); 
    } catch (error) {
      alert("Failed to update status.");
    }
  }
async downloadResume(id: string) {
  try {
    const response = await api.get(`/resume/download/${id}`, {
      responseType: 'blob', // Crucial for @Lob data
      withCredentials: true 
    });

    // Create a local URL for the binary data
    const blob = new Blob([response.data], { type: 'application/pdf' });
    const url = window.URL.createObjectURL(blob);
    
    // Create a temporary hidden link and click it
    const link = document.createElement('a');
    link.href = url;
    link.download = `resume_${id}.pdf`;
    document.body.appendChild(link);
    link.click();
    
    // Cleanup
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  } catch (error: any) {
    console.error("Server Error (500): Check if the file exists in the DB.", error);
  }
}
}
