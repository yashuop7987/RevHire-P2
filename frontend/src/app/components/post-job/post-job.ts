import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import api from '../service/axios';

@Component({
  selector: 'app-post-job',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './post-job.html',
  styleUrls: ['./post-job.css']
})
export class PostJobComponent implements OnInit {

  jobForm!: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.jobForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', [Validators.required, Validators.maxLength(3000)]],
      skills: ['', Validators.required],
      experienceYears: [0, [Validators.required, Validators.min(0)]],
      education: ['', Validators.required],
      location: ['', Validators.required],
      salary: [0, [Validators.required, Validators.min(0)]],
      jobType: ['', Validators.required],
      deadline: ['', Validators.required]
      
    });
  }

  async onSubmit() {

    if (this.jobForm.invalid) {
      this.jobForm.markAllAsTouched();
      return;
    }

    try {
      this.loading = true;

      const formData = { ...this.jobForm.value, active: true };

      // Convert to Spring LocalDateTime format
      if (formData.deadline) {
        formData.deadline = formData.deadline.length === 16
          ? `${formData.deadline}:00`
          : formData.deadline;
      }

      console.log("Sending Data:", formData);

      await api.post('/employer/job', formData);

      alert('Job Posted Successfully ✅');
      this.router.navigate(['/employer/dashboard']);

    } catch (error: any) {
      console.error("Error:", error);

      if (error.response?.status === 403) {
        alert("Access Denied. Only Employers can post jobs.");
      } else if (error.response?.status === 400) {
        alert("Validation error. Check backend console.");
      } else {
        alert("Something went wrong.");
      }

    } finally {
      this.loading = false;
    }
  }
}
