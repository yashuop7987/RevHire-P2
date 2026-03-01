import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import api from '../service/axios'; // Adjust path to your axios instance

@Component({
  selector: 'app-seeker-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './seeker-profile.html',
  styleUrls: ['./seeker-profile.css']
})
export class SeekerProfileComponent implements OnInit {
  profileForm!: FormGroup;
  isLoading = true;
  isNewUser = true; // Flag to track if we are Creating or Updating

  constructor(private fb: FormBuilder, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      location: ['', Validators.required],
      employmentStatus: ['', Validators.required]
    });

    this.loadProfile();
  }

  async loadProfile() {
    try {
      const response = await api.get('/jobseeker/profile');
      if (response.data) {
        this.profileForm.patchValue(response.data);
        this.isNewUser = false; // Data found, switch to Update mode
      }
    } catch (error: any) {
      if (error.response?.status === 404) {
        this.isNewUser = true; // No profile yet, stay in Create mode
      }
    } finally {
      this.isLoading = false;
      this.cdr.detectChanges();
    }
  }

  async onSubmit() {
    if (this.profileForm.invalid) return;

    const profileData = this.profileForm.getRawValue();
    try {
      if (this.isNewUser) {
        // STEP 1: CREATE (POST)
        await api.post('/jobseeker/profile', profileData);
        alert('Profile Created Successfully!');
        this.isNewUser = false; 
      } else {
        // STEP 2: UPDATE (PUT)
        await api.put('/jobseeker/profile', profileData);
        alert('Profile Updated Successfully!');
      }
    } catch (error) {
      alert('Action failed. Check console for details.');
      console.error(error);
    }
  }

  // Progress Bar Logic
  get completionPercentage(): number {
    const controls = this.profileForm.controls;
    const total = Object.keys(controls).length;
    let filled = 0;
    for (const key in controls) {
      if (controls[key].value) filled++;
    }
    return Math.round((filled / total) * 100);
  }
}