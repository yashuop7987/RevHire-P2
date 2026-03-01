import { Component, OnInit } from '@angular/core';
import { CommonModule} from '@angular/common';
import api from '../service/axios';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-process-applications',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './process-applications.html'
})
export class ProcessApplicationsComponent implements OnInit {
  applications: any[] = [];
  filteredApplications: any[] = []; // List to display in the UI
  isLoading: boolean = true;
  employerId: number | null = null;

  // Search and Filter variables
  searchTerm: string = '';
  statusFilter: string = 'ALL';

  async ngOnInit() {
    await this.initializeEmployerData();
  }

  async initializeEmployerData() {
    this.isLoading = true;
    try {
      const profileRes = await api.get('/employer/profile');
      this.employerId = profileRes.data.id;

      if (this.employerId) {
        const appRes = await api.get(`/employer/applicants/${this.employerId}`);
        this.applications = appRes.data;
        this.applyFilters(); // Initialize the filtered list
      }
    } catch (error) {
      console.error("Initialization failed:", error);
    } finally {
      this.isLoading = false;
    }
  }

  // The Search Engine
  applyFilters() {
    this.filteredApplications = this.applications.filter(app => {
      const matchesSearch = 
        app.jobSeeker?.name?.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        app.job?.title?.toLowerCase().includes(this.searchTerm.toLowerCase());

      const matchesStatus = 
        this.statusFilter === 'ALL' || app.status === this.statusFilter;

      return matchesSearch && matchesStatus;
    });
  }

  async updateStatus(appId: number, status: string) {
    try {
      await api.put(`/employer/application/${appId}/status`, null, {
        params: { status: status }
      });
      alert(`Candidate status updated to ${status}`);
      await this.initializeEmployerData(); 
    } catch (error) {
      alert("Failed to update status.");
    }
  }
}