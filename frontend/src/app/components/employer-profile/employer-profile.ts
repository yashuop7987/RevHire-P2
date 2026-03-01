import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import api from '../service/axios'; // correct path
import { ChangeDetectorRef } from '@angular/core';
import { Signal } from '@angular/core';
import { SIGNAL } from '@angular/core/primitives/signals';

@Component({
  selector: 'app-employer-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employer-profile.html',
  styleUrls: ['./employer-profile.css']
})
export class EmployerProfileComponent implements OnInit {
  constructor(private cdr: ChangeDetectorRef) {}


employer:any = {
  companyName: '',
  industry: '',
  companySize: '',
  website: '',
  location: '',
  email: '',
  description: ''
};





  editMode = false;
  loading = false;
 user_id =0;
  ngOnInit(): void {
    this.loadProfile();
  }

  async loadProfile() {
  try {
    this.loading = true;
    const response = await api.get('/employer/profile');

    this.user_id=response.data.id;

    // CRITICAL CHECK: Only assign if response.data is a real object
    if (response.data && typeof response.data === 'object' && !Array.isArray(response.data)) {
      this.employer = response.data;
      this.cdr.detectChanges();
    } else {
      console.warn("Backend returned empty or invalid data, keeping default object.");
      this.resetToDefault();
    }

    
  } catch (error: any) {
    console.error("Error loading profile:", error);
    this.resetToDefault();
  } finally {
    this.loading = false;
  }
}

// Ensure this method exists to reset your structure
resetToDefault() {
  this.employer = {
    companyName: '',
    industry: '',
    companySize: '',
    website: '',
    location: '',
    email: '',
    description: ''
  };
}

  async saveProfile() {
    try {
      this.loading = true;

      await api.put(`/employer/profile`, this.employer);

      alert("Profile Updated Successfully ✅");
      this.editMode = false;

    } catch (error: any) {
      console.error("Update failed", error);
      alert("Failed to update profile.");
    } finally {
      this.loading = false;
    }
  }
}
