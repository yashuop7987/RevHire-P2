import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import api from '../service/axios';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html'
})
export class LoginComponent {

  loginData = {
    username: '',
    password: ''
  };

  constructor(private router: Router) {}   // ✅ removed ApiService

  async login() {
    try {
      const response = await api.post('/auth/login', this.loginData);
      const data = response.data;

      // Save user info
      localStorage.setItem('token', data.token);
      localStorage.setItem('role', data.role);
      localStorage.setItem('username', data.username);

      console.log("Login successful. Role:", data.role);

      // Routing logic
      let targetRoute = '';

      if (data.role === 'ROLE_EMPLOYER') {
        targetRoute = '/employer/dashboard';
      } 
      else if (data.role === 'ROLE_JOBSEEKER') {
        targetRoute = '/jobseeker/menu';
      }

      this.router.navigateByUrl(targetRoute);

    } catch (error) {
      console.error('Login Error:', error);
      alert('Invalid credentials');
    }
  }
}
