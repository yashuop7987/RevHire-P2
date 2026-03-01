import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import api from '../service/axios';

@Component({
  selector: 'app-edit-job',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-job.html',
  styleUrls: ['./edit-job.css']
})
export class EditJobComponent implements OnInit {
  editForm!: FormGroup;
  jobId!: number;
  isLoading = true;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  goBack() {
  this.router.navigate(['/employer/manage-jobs']);
}

  ngOnInit(): void {
    // 1. Get ID from URL (/employer/edit-job/5)
    this.jobId = Number(this.route.snapshot.paramMap.get('id'));

    this.editForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', [Validators.required, Validators.maxLength(3000)]],
      skills: ['', Validators.required],
      experienceYears: [0, [Validators.required, Validators.min(0)]],
      education: ['', Validators.required],
      location: ['', Validators.required],
      salary: [0.0, [Validators.required, Validators.min(0)]],
      jobType: ['', Validators.required],
      deadline: ['', Validators.required],
      active: [true]
    });

    this.loadJobData();
  }

  async loadJobData() {
    try {
      const response = await api.get(`/employer/job/${this.jobId}`);
      const job = response.data;

      // Format date for datetime-local input (YYYY-MM-DDTHH:mm)
      if (job.deadline) {
        job.deadline = job.deadline.substring(0, 16);
      }

      // 2. Pre-fill the form with existing data
      this.editForm.patchValue(job);
      this.isLoading = false;
      this.cdr.detectChanges();
    } catch (error) {
      console.error('Failed to load job:', error);
      alert('Could not find this job posting.');
      this.router.navigate(['/employer/manage-jobs']);
    }
  }

  async onUpdate() {
    if (this.editForm.valid) {
      try {
        const formData = { ...this.editForm.value };
        
        // Ensure date format is correct for LocalDateTime
        if (formData.deadline && formData.deadline.length === 16) {
          formData.deadline = `${formData.deadline}:00`;
        }

        await api.put(`/employer/job/${this.jobId}`, formData);
        alert('Job Updated Successfully!');
        this.router.navigate(['/employer/manage-jobs']);
      } catch (error) {
        console.error('Update failed:', error);
        alert('Error updating job. Check data types.');
      }
    }
  }
}