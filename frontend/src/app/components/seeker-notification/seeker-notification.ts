import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import api from '../service/axios';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-seeker-notification',
  imports: [CommonModule, RouterModule],
  templateUrl: './seeker-notification.html',
  styleUrl: './seeker-notification.css',
})
export class SeekerNotification {
  unreadCount: number = 0;
  notifications: any[] = [];
  showDropdown: boolean = false;

  constructor(private cdr: ChangeDetectorRef) {}

  async ngOnInit() {
    await this.loadNotifications();
  }

 async loadNotifications() {
  try {
    const profileRes = await api.get('/jobseeker/profile');
    // Extract the nested user ID to match your @ManyToOne user relationship
    const userId = profileRes.data.user.id; 
    
    const response = await api.get(`/jobseeker/notifications/${userId}`);
    this.notifications = response.data;
    console.log(response.data);
    this.cdr.detectChanges();
    this.unreadCount = this.notifications.filter(n => !n.isRead).length;
  } catch (error) {
    console.error("Could not load notifications", error);
  }
}

async markAllAsRead() {
  try {
    const profileRes = await api.get('/jobseeker/profile');
    const userId = profileRes.data.user.id; // Use user.id here as well
    
    await api.put(`/jobseeker/notifications/read-all/${userId}`);
    await this.loadNotifications(); 

  } catch (error) {
    console.error("Failed to clear notifications", error);
  }
}

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }
  // Add these methods inside your SeekerNotification class

async markAsRead(id: number) {
  try {
    // Call your Spring Boot PUT endpoint
    await api.put(`/jobseeker/notifications/${id}/read`);
    
    // Update local state without full reload for better UX
    const note = this.notifications.find(n => n.id === id);
    if (note) note.isRead = true;
    
    // Recalculate unread count
    this.unreadCount = this.notifications.filter(n => !n.isRead).length;
  } catch (error) {
    console.error("Failed to mark notification as read", error);
  }
}

}
