package com.application.portal.Service;


import com.application.portal.Model.Job;
import com.application.portal.Model.JobSeeker;
import com.application.portal.Model.SavedJobs;
import com.application.portal.Repository.JobRepository;
import com.application.portal.Repository.JobSeekerRepository;
import com.application.portal.Repository.SavedJobRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedJobService {
    @Autowired
    private SavedJobRepository savedJobRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobSeekerRepository profileRepository;

    @Transactional
    public void saveJob(Long jobId, Long seekerId) {
        // 1. Prevent duplicate saves
        if (savedJobRepository.existsByJobIdAndJobSeekerId(jobId, seekerId)) {
            throw new RuntimeException("You have already saved this job.");
        }

        // 2. Fetch references (Standard JPA findById)
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        JobSeeker profile = profileRepository.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // 3. Create and save the relationship
        SavedJobs savedJob = new SavedJobs();
        savedJob.setJob(job);
        savedJob.setJobSeeker(profile);
        savedJob.setSavedAt(LocalDateTime.now());

        savedJobRepository.save(savedJob);
    }

    public List<Job> getSavedJobsBySeeker(Long seekerId) {
        // Fetch the SavedJob entries and extract the Job objects from them
        return savedJobRepository.findByJobSeekerId(seekerId)
                .stream()
                .map(SavedJobs::getJob)
                .collect(Collectors.toList());
    }
    public boolean isAlreadySaved(Long jobId, Long seekerId) {
        return savedJobRepository.existsByJobIdAndJobSeekerId(jobId, seekerId);
    }

    @Transactional
    public void unsaveJob(Long jobId, Long seekerId) {
        savedJobRepository.deleteByJob_IdAndJobSeeker_Id(jobId, seekerId);
    }
    @Transactional
    public void removeSavedJob(Long jobId, Long seekerId) {
        // Calling the repository method to delete by the composite identifiers
        savedJobRepository.deleteByJob_IdAndJobSeeker_Id(jobId, seekerId);
    }
}
