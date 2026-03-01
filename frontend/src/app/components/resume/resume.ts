import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import api from '../service/axios';

@Component({
  selector: 'app-resume',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './resume.html',
  styleUrls: ['./resume.css']
})
export class ResumeComponent implements OnInit {
  resumeForm!: FormGroup;
  profileId: number = 0; // You should get this from your Auth/Profile state

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.resumeForm = this.fb.group({
      objective: ['', [Validators.required, Validators.maxLength(2000)]],
      education: ['', [Validators.required, Validators.maxLength(3000)]],
      experience: ['', [Validators.required, Validators.maxLength(3000)]],
      skills: ['', [Validators.required, Validators.maxLength(2000)]],
      projects: ['', [Validators.required, Validators.maxLength(3000)]],
      certifications: ['', [Validators.maxLength(2000)]]
    });

    this.loadInitialData();
  }

async loadInitialData() {
  try {
    const profileRes = await api.get('/jobseeker/profile');
    console.log("Profile Data received:", profileRes.data); // DEBUG 1
    
    // Check if ID exists. If your backend uses 'jobSeekerId' instead of 'id', change this!
    this.profileId = profileRes.data.id; 

    if (!this.profileId) {
       console.error("Profile ID is missing in the response!");
    }

    const resumeRes = await api.get(`/jobseeker/my-resume/${this.profileId}`);
    console.log("Resume Data received:", resumeRes.data); // DEBUG 2
    
    if (resumeRes.data) {
      this.resumeForm.patchValue(resumeRes.data);
    }
  } catch (error) {
    console.error("An error occurred during loading:", error);
  } finally {
    // This MUST be reached. If it isn't, the spinner stays.
    console.log("Stopping spinner...");
  }
}
  async saveResume() {
    if (this.resumeForm.invalid) return;

    try {
      const response = await api.post(`/jobseeker/save/${this.profileId}`, this.resumeForm.value);
      alert('Resume saved successfully!');
    } catch (error) {
      console.error(error);
      alert('Failed to save resume.');
    }
  }
}