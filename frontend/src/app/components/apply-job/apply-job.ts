import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import api from '../service/axios';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-apply-job',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './apply-job.html'
})
export class ApplyJobComponent implements OnInit {
  @Input() jobId!: number;
  @Input() seekerId!: number;
  
  useSaved: boolean = true;
  resumeFile: File | null = null;
  coverLetter: string = '';
  isSubmitting: boolean = false;

  constructor(private route: ActivatedRoute) {}

  async ngOnInit() {
    
    // 1. Fetch James's profile ID if not provided by a parent
    if (!this.seekerId) {
      try {
        const profileRes = await api.get('/jobseeker/profile');
        this.seekerId = profileRes.data.id;
      } catch (error) {
        console.error("Error fetching James's profile:", error);
      }
    }

    // 2. Fetch Job ID from the URL (?jobId=X) if not provided by a parent
    if (!this.jobId) {
      this.route.queryParams.subscribe(params => {
        if (params['jobId']) {
          this.jobId = +params['jobId']; // The '+' converts string to number
        }
      });
    }
  }

  async submitApplication() {
    // Safety check updated to ensure IDs exist
    if (!this.jobId || !this.seekerId) {
      console.error("Missing required IDs:", { jobId: this.jobId, seekerId: this.seekerId });
      alert("Required information is missing. Please return to the search page.");
      return;
    }

    this.isSubmitting = true;
    const formData = new FormData();
    
    formData.append('jobId', this.jobId.toString());
    formData.append('seekerId', this.seekerId.toString());
    formData.append('coverLetter', this.coverLetter);
    formData.append('useSaved', String(this.useSaved));

    if (!this.useSaved && this.resumeFile) {
      formData.append('resumeFile', this.resumeFile);
    }

    try {
      await api.post('jobseeker/apply', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      alert("Application Successful!");
      // Option: Navigate to My Applications here
    } catch (error: any) {
      alert(error.response?.data || "Failed to apply");
    } finally {
      this.isSubmitting = false;
    }
  }

  onFileSelected(event: any) {
    this.resumeFile = event.target.files[0];
  }
}