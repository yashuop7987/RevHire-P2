# RevHire – Job Portal Application

## Project Overview
RevHire is a full-stack monolithic web application that connects job seekers with employers. The platform allows job seekers to build profiles, create and upload resumes, search for jobs using advanced filters, and apply for job openings. Employers can post job opportunities, review applications, and manage recruitment workflows.

The system provides a responsive user interface, secure authentication, role-based access control, and real-time notifications for application updates.

---

## Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- REST API
- Maven

### Frontend
- Angular
- TypeScript
- HTML
- CSS
- Bootstrap

### Database
- MySQL

### Tools
- Git
- Docker
- Postman
- Maven

---

## Application Architecture

The application follows a layered monolithic architecture:

Angular Frontend  
⬇  
Spring Boot REST API  
⬇  
Service Layer  
⬇  
Repository Layer (Spring Data JPA)  
⬇  
MySQL Database

---

## Job Seeker Features

- User registration and login
- Profile creation and management
- Resume builder with structured sections
- Upload resume (PDF/DOCX up to 2MB)
- Advanced job search with filters
- One-click job application
- Application tracking with status updates
- Withdraw job applications
- Save jobs to favourites
- View job descriptions and company profiles
- Receive in-app notifications for application updates

---

## Employer Features

- Company registration and login
- Create and publish job postings
- Manage job postings (edit, close, delete)
- View applicants for each job
- Shortlist or reject candidates
- Bulk application management
- Filter applicants by experience, skills, and status
- Manage company profile
- Dashboard with job and application statistics
- Add notes to applications for internal tracking

---

## Core Functional Modules

### Authentication & Account Management
Secure login and registration system with role-based access control for job seekers and employers.

### Resume Management
Allows job seekers to build structured resumes and upload formatted resumes.

### Job Management
Employers can create and manage job postings, while job seekers can search and apply for jobs.

### Application Management
Tracks job applications and allows employers to shortlist or reject candidates.

### Notification System
Provides real-time notifications for job applications and status updates.

---

## Entity Relationship Diagram (ERD)

Main Entities:

- User
- Employer
- Job
- Application
- Resume
- Notification
- FavouriteJob

Relationships:

- A job seeker can apply to multiple jobs.
- A job can have multiple applications.
- Employers can create multiple job postings.
- Applications connect job seekers with job postings.

---

## Installation & Setup

### Clone the Repository

```bash
git clone https://github.com/your-username/revhire.git
cd revhire
