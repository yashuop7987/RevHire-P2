package com.application.portal.Service;

import com.application.portal.Dto.EmployerStatsDTO;
import com.application.portal.Model.ApplicationStatus;
import com.application.portal.Model.Employer;
import com.application.portal.Model.Job;
import com.application.portal.Repository.ApplicationRepository;
import com.application.portal.Repository.EmployerRepository;
import com.application.portal.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private JobRepository jobRepository;
    private EmployerRepository employerProfileRepository;
    private ApplicationRepository applicationRepository;
    private NotificationService notificationService;

    @Autowired
    public JobService(NotificationService notificationService, JobRepository jobRepository, EmployerRepository employerProfileRepository, ApplicationRepository applicationRepository)
    {
        this.jobRepository=jobRepository;
        this.employerProfileRepository=employerProfileRepository;
        this.applicationRepository=applicationRepository;
        this.notificationService=notificationService;
    }



    public Job createJob(Job job, String username) {

        Employer employer = employerProfileRepository
                .findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Employer profile not found"));

        job.setEmployer(employer);
        job.setActive(true);
        notificationService.notifyAllUsersAboutJob(job);
        return jobRepository.save(job);
    }



    // Get All Active Jobs (For JobSeeker)
    public List<Job> getAllActiveJobs() {
        return jobRepository.findByActiveTrue();
    }

    // Get Jobs By Logged-In Employer
    public List<Job> getMyJobs(String username) {
       Optional<Employer> e= employerProfileRepository.findByUserUsername(username);

       return jobRepository.findByEmployer(e.get());
    }


    public List<Job> getJobsStatus(boolean active,Long id) {
        return jobRepository.findByEmployerIdAndActive(id,active);
    }

    // Update Job
    public Job updateJob(Long id, Job updatedJob, String username) {

        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!existingJob.getEmployer().getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized action");
        }

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setSkills(updatedJob.getSkills());
        existingJob.setExperienceYears(updatedJob.getExperienceYears());
        existingJob.setEducation(updatedJob.getEducation());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setSalary(updatedJob.getSalary());
        existingJob.setJobType(updatedJob.getJobType());
        existingJob.setDeadline(updatedJob.getDeadline());
        existingJob.setActive(updatedJob.isActive());

        return jobRepository.save(existingJob);
    }

    // Soft Delete (Recommended)
    public Job toggleJobStatus(Long id, boolean status) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setActive(status);
        return jobRepository.save(job);
    }

    public Job updateJob(Long id, Job jobDetails) {
        // 1. Find existing job
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

        // 2. Update the fields
        existingJob.setTitle(jobDetails.getTitle());
        existingJob.setDescription(jobDetails.getDescription());
        existingJob.setSkills(jobDetails.getSkills());
        existingJob.setExperienceYears(jobDetails.getExperienceYears());
        existingJob.setEducation(jobDetails.getEducation());
        existingJob.setLocation(jobDetails.getLocation());
        existingJob.setSalary(jobDetails.getSalary());
        existingJob.setJobType(jobDetails.getJobType());
        existingJob.setDeadline(jobDetails.getDeadline());
        existingJob.setActive(jobDetails.isActive()); // Ensure the active status is preserved or updated

        // 3. Save the updated record
        return jobRepository.save(existingJob);
    }


    public boolean deleteJob(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Job> findJobsByCriteria(String title, String location, Integer experience, String jobType) {
        // Business Logic: You could add logging or default values here
        return jobRepository.searchJobs(title, location, experience, jobType);
    }
    public Optional<Job> getJobById(Long jobId)
    {
        return jobRepository.findById(jobId);
    }
    public EmployerStatsDTO getEmployerStats(Long employerId) {
        long totalJobs = jobRepository.countByEmployer_Id(employerId);
        boolean status = true;
        long activeOpenings = jobRepository.countByEmployer_IdAndActiveTrue(employerId);
        long totalApps = applicationRepository.countByJob_Employer_Id(employerId);
        long pending = applicationRepository.countByJob_Employer_IdAndStatus(employerId, ApplicationStatus.APPLIED);

        return new EmployerStatsDTO(totalJobs, activeOpenings, totalApps, pending);
    }

}
