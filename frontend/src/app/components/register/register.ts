import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import api from '../service/axios'; // correct path

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html'
})
export class RegisterComponent {

  registerData = {
    username: '',
    password: '',
    role: '',
  };

  constructor(private router: Router) {}  // ❌ removed api injection

  async register() {
    try {
      const response = await api.post('/auth/register', this.registerData);

      if (response.status === 200 || response.status === 201) {
        alert('Registration successful');
        this.router.navigate(['/login']);
      }

    } catch (error: any) {
      console.error('Registration Error:', error);
      const errorMessage =
        error.response?.data?.message ||
        'Registration failed. Please try again.';
      alert(errorMessage);
    }
  }
}
