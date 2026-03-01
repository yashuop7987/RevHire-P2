package com.application.portal.Controller;

import com.application.portal.Model.ApplicationStatus;
import com.application.portal.Repository.ApplicationRepository;
import com.application.portal.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmployerStatsController {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("employer/stats/{employerId}")
    public ResponseEntity<Map<String, Long>> getDashboardStats(@PathVariable String employerId) {
        Map<String, Long> stats = new HashMap<>();

        // Using the count methods from your repository
        stats.put("totalJobs", jobRepository.countByEmployer_Id(Long.valueOf(employerId)));
        stats.put("activeJobs", jobRepository.countByEmployer_IdAndActiveTrue(Long.valueOf(employerId)));
        stats.put("totalApplications", applicationRepository.countByJob_Employer_Id(Long.valueOf(employerId)));
        stats.put("pendingReviews", applicationRepository.countByJob_Employer_IdAndStatus(Long.valueOf(employerId), ApplicationStatus.APPLIED));

        return ResponseEntity.ok(stats);
    }
}
