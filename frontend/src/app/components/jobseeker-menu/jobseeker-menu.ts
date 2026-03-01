import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router,RouterOutlet,RouterLinkActive,RouterLink } from '@angular/router';
import api from '../service/axios';



@Component({
  selector: 'app-jobseeker-menu',
  imports: [RouterOutlet,RouterLinkActive,RouterOutlet,CommonModule,RouterLink],
  templateUrl: './jobseeker-menu.html',
  styleUrl: './jobseeker-menu.css',
})
export class JobseekerMenu {
  notifications: any[] = [];
  unreadCount: number = 0;
  userName: string = localStorage.getItem('username')?.toUpperCase() || 'Job Seeker';
   // This would ideally come from your Auth service

  constructor(private router: Router) {}
  

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
  }
  async loadUnreadCount() {
    try {
      const profileRes = await api.get('/jobseeker/profile');
      const userId = profileRes.data.user.id;
      // Fetch notifications linked to this User ID
      const response = await api.get(`/api/notifications/user/${userId}`);
      // Filter for notifications where isRead is false
      this.unreadCount = response.data.filter((n: any) => !n.isRead).length;
    } catch (error) {
      console.error("Error loading notification count", error);
    }
  }

}
