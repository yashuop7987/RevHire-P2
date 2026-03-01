package com.application.portal.Controller;

import com.application.portal.Dto.JobDTO;
import com.application.portal.Exception.JobNotFound;
import com.application.portal.Model.Job;
import com.application.portal.Repository.JobRepository;
import com.application.portal.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class JobController {

     private JobService jobService;

     private JobRepository jobRepository;


    private String getLoggedInUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }



    @Autowired
     public  JobController(JobService jobService,JobRepository jobRepository)
     {
         this.jobService=jobService;
         this.jobRepository=jobRepository;
     }

    @PostMapping("/employer/job")
    public ResponseEntity<?> createJob(@RequestBody JobDTO jobDTO, Authentication authentication)
    {
        String username = authentication.getName();

        try {


            // 2. Find the EmployerProfile associated with this user
            // (Assuming your User and EmployerProfile are linked by username)

            // 3. Establish the relationship
            Job job = new Job();
            job.setJobType(jobDTO.getJobType());
            job.setDescription(jobDTO.getDescription());
            job.setLocation(jobDTO.getLocation());
            job.setDeadline(jobDTO.getDeadline());
            job.setActive(jobDTO.isActive());
            job.setEducation(jobDTO.getEducation());
            job.setExperienceYears(jobDTO.getExperienceYears());
            job.setSalary(jobDTO.getSalary());
            job.setSkills(jobDTO.getSkills());
            job.setTitle(jobDTO.getTitle());

            System.out.println(job.toString());

            // 4. Save the job (createdAt is handled by @PrePersist)
            return ResponseEntity.ok(jobService.createJob(job, username));

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/employer/jobs")
    public ResponseEntity<List<Job>> getAllJobs(Authentication authentication) {
        try {
            System.out.println("Authentication: " + authentication);
            System.out.println("Authorities: " + authentication.getAuthorities());
            List<Job>  jobs =jobService.getMyJobs(getLoggedInUsername());

            return ResponseEntity.status(HttpStatus.OK).body(jobs);
        }catch (JobNotFound e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }


    @GetMapping("/employer/job/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employer/jobs/{id}/status")
    public ResponseEntity<List<Job>> getJobsByStatus(@RequestParam boolean active,@PathVariable Long id) {
        // Logic to get the current employer's ID from the SecurityContext
        // For now, fetching all jobs by status:
        List<Job> jobs = jobService.getJobsStatus(active,id);

        if (jobs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobs);
    }


    @DeleteMapping("/employer/jobs/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {


        boolean isDeleted= jobService.deleteJob(id);

        if (isDeleted) {
            return ResponseEntity.ok("Job deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Job not found with id: " + id);
        }
    }

    @PutMapping("/employer/jobs/{id}/status")
    public ResponseEntity<?> updateJobStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> statusMap) {
        try {
            // Extract the boolean from the request body
            Boolean newStatus = statusMap.get("active");

            if (newStatus == null) {
                return ResponseEntity.badRequest().body("Status 'active' is required");
            }

            Job updatedJob = jobService.toggleJobStatus(id, newStatus);
            return ResponseEntity.ok(updatedJob);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/jobseeker/jobs")
    public ResponseEntity<?> getAllJobs()
    {
        List<Job> jobs = jobService.getAllActiveJobs();

        if(jobs!=null)
        {
            return ResponseEntity.ok(jobs);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }




    @PutMapping("/employer/job/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        try {
            Job updatedJob = jobService.updateJob(id,jobDetails);
            return ResponseEntity.ok(updatedJob);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating job");
        }
    }

    @GetMapping("/jobseeker/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer experience,
            @RequestParam(required = false) String jobType) {

        List<Job> results = jobService.findJobsByCriteria(title, location, experience, jobType);
        return ResponseEntity.ok(results);
    }
    @GetMapping("/jobseeker/job/{id}")
    public ResponseEntity<?> getAJobById(@PathVariable Long id) {
        try {
            Optional<Job> job = jobService.getJobById(id);
            return ResponseEntity.ok(job.get());
        } catch (RuntimeException e) {
            // Return 404 if the job doesn't exist to stop the frontend from hanging
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
