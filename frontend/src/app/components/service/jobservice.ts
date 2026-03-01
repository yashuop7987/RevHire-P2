import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private apiUrl = 'http://localhost:8080/jobseeker/search';

  constructor(private http: HttpClient) { }

  getFilteredJobs(filters: any): Observable<any[]> {
    let params = new HttpParams();

    // Dynamically append parameters only if they have a value
    if (filters.title) params = params.set('title', filters.title);
    if (filters.location) params = params.set('location', filters.location);
    if (filters.experience) params = params.set('experience', filters.experience.toString());
    if (filters.jobType) params = params.set('jobType', filters.jobType);

    return this.http.get<any[]>(this.apiUrl, { params });
  }
}