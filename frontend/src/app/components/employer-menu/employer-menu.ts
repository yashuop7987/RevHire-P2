import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import api from '../service/axios';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-employer-menu',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './employer-menu.html',
  styleUrls: ['./employer-menu.css']
})
export class EmployerMenuComponent implements OnInit {
  stats = {
    totalJobs: 0,
    activeJobs: 0,
    totalApplications: 0,
    pendingReviews: 0
  };

  companyName: string = '';
  username: string = '';

  constructor(private router: Router,private cdr:ChangeDetectorRef) {}

  async ngOnInit() {
    // 1. Immediate local data
    this.username = localStorage.getItem('username') || 'Employer';

    try {
      // 2. Fetch Profile (to get company name and employer ID)
      const profileRes = await api.get('/employer/profile');
      const employerId = profileRes.data.id;
      console.log(profileRes.data);

      // 3. Fetch Statistics using the dynamic ID
      const statsRes = await api.get(`/employer/stats/${employerId}`);
      this.stats = statsRes.data;
      this.cdr.detectChanges();

    } catch (error) {
      console.error("Failed to load dashboard data", error);
      this.companyName = this.username; // Fallback
    }
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}