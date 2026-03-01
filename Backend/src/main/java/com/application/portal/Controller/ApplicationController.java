package com.application.portal.Controller;

import com.application.portal.Model.*;
import com.application.portal.Service.ApplicationService;
import com.application.portal.Service.JobSeekerService;
import com.application.portal.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {



    private ApplicationService applicationService;
    private JobSeekerService jobSeekerService;
    private JobService jobService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, JobSeekerService jobSeekerService, JobService jobService)
    {

        this.applicationService=applicationService;
        this.jobSeekerService = jobSeekerService;
        this.jobService=jobService;
    }


        @PostMapping(value = "jobseeker/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<?> applyToJob(
            @RequestParam Long jobId,
            @RequestParam Long seekerId,
            @RequestParam String coverLetter,
        @RequestParam boolean useSaved,
        @RequestParam(required = false) MultipartFile resumeFile) {

        try {
            // Logic to save application and link resume
            applicationService.processApplication(jobId, seekerId, coverLetter, useSaved, resumeFile);
            return ResponseEntity.ok("Application Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("jobseeker/my-applications/{seekerId}")
    public ResponseEntity<List<Application>> getMyApplications(@PathVariable Long seekerId) {
        List<Application> apps = applicationService.getApplicationsBySeeker(seekerId);
        return ResponseEntity.ok(apps);
    }


    @DeleteMapping("jobseeker/withdraw-application/{applicationId}")
    public ResponseEntity<?> withdrawApplication(@PathVariable Long applicationId) {
        try {
            applicationService.deleteApplication(applicationId);
            return ResponseEntity.ok("Application withdrawn successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not withdraw application: " + e.getMessage());
        }
    }

    @GetMapping("employer/applicants/{employerId}")
    public ResponseEntity<List<Application>> getApplicants(@PathVariable Long employerId) {
        return ResponseEntity.ok(applicationService.getApplicantsForEmployer(employerId));
    }
    @PutMapping("employer/application/{appId}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long appId,
            @RequestParam ApplicationStatus status) {
        try {
            applicationService.updateStatus(appId, status);
            return ResponseEntity.ok("Status updated to " + status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating status: " + e.getMessage());
        }
    }
}

