package com.application.portal.Service;

import com.application.portal.Model.*;
import com.application.portal.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ApplicationService {


    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private FileResumeRepository fileResumeRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobSeekerRepository seekerRepository;


    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public void processApplication(Long jobId, Long seekerId, String coverLetter,
                                   boolean useSaved, MultipartFile resumeFile) throws IOException {

        // 1. Validate if James already applied
        if (applicationRepository.existsByJobIdAndJobSeekerId(jobId, seekerId)) {
            throw new RuntimeException("You have already applied for this position.");
        }

        // 2. Fetch Job and Seeker entities
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        JobSeeker seeker = seekerRepository.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // 3. If James uploaded a new file, save/update his FileResume
        if (!useSaved && resumeFile != null && !resumeFile.isEmpty()) {
            FileResume fileResume = fileResumeRepository.findByJobSeekerId(seekerId)
                    .orElse(new FileResume());

            fileResume.setFileName(resumeFile.getOriginalFilename());
            fileResume.setFileType(resumeFile.getContentType());
            fileResume.setData(resumeFile.getBytes());
            fileResume.setJobSeeker(seeker);

            fileResumeRepository.save(fileResume);
        }

        // 4. Create the Application record
        Application application = Application.builder()
                .job(job)
                .jobSeeker(seeker)
                .coverLetter(coverLetter)
                .status(ApplicationStatus.APPLIED) // Default status
                .appliedAt(LocalDateTime.now()) // Set current timestamp
                .build();

        applicationRepository.save(application);
    }

    public List<Application> getApplicationsBySeeker(Long seekerId) {
        return applicationRepository.findByJobSeekerId(seekerId);
    }

    @Transactional
    public void deleteApplication(Long applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new RuntimeException("Application not found.");
        }
        applicationRepository.deleteById(applicationId);
    }

    public List<Application> getApplicantsForEmployer(Long employerId) {
        // Fetches all applicants for all jobs posted by this employer
        return applicationRepository.findAllByJob_Employer_Id(employerId);
    }

    @Transactional
    public void updateStatus(Long appId, ApplicationStatus newStatus) {
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        app.setStatus(newStatus);
        // applicationRepository.save(app); // Optional with @Transactional
        Notification note = Notification.builder()
                .user(app.getJobSeeker().getUser()) // Link to User entity
                .message("Job Update: Your application for " + app.getJob().getTitle() + " is now " + app.getStatus())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(note);
    }


}
