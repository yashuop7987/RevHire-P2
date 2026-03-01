import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import api from '../service/axios';

@Component({
  selector: 'app-seeker-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './seeker-dashboard.html' // Moving to external HTML for cleanliness
})
export class SeekerDashboardComponent implements OnInit {
  appliedCount: number = 0;
  shortlistedCount: number = 0;
  savedCount: number = 0;
  isLoading: boolean = true;


  constructor(private cdr:ChangeDetectorRef){}
  async ngOnInit() {
    await this.loadStats();
    this.cdr.detectChanges();
  }

  async loadStats() {
    this.isLoading = true;
    try {
      // 1. Get Seeker ID for James
      const profileRes = await api.get('/jobseeker/profile');
      const seekerId = profileRes.data.id;

      // 2. Fetch data in parallel
      const [appRes, savedRes] = await Promise.all([
        api.get(`/jobseeker/my-applications/${seekerId}`),
        api.get(`/jobseeker/my-saved-jobs/${seekerId}`),
        //status api
      ]);

      // 3. Update dynamic values
      this.appliedCount = appRes.data.length;
      this.savedCount = savedRes.data.length;
      

      // 4. Calculate shortlisted (assuming status is 'ACCEPTED')
      this.shortlistedCount = appRes.data.filter((app: any) => app.status === 'ACCEPTED').length;
      this.cdr.detectChanges();

    } catch (error) {
      console.error("Dashboard load failed:", error);
    } finally {
      this.isLoading = false;
    }
  }
}