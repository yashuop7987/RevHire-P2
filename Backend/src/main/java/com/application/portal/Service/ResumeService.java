package com.application.portal.Service;

import com.application.portal.Model.FileResume;
import com.application.portal.Model.JobSeeker;
import com.application.portal.Model.Resume;
import com.application.portal.Repository.FileResumeRepository;
import com.application.portal.Repository.JobSeekerRepository;
import com.application.portal.Repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private FileResumeRepository fileResumeRepository;

    @Autowired
    private JobSeekerRepository profileRepository;

    public Resume saveOrUpdateResume(Long profileId, Resume resumeDto) {
        // 1. Find the profile first
        JobSeeker profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // 2. Check if a resume already exists for this profile
        return resumeRepository.findByJobSeekerId(profileId)
                .map(existingResume -> {
                    // UPDATE existing resume
                    existingResume.setObjective(resumeDto.getObjective());
                    existingResume.setEducation(resumeDto.getEducation());
                    existingResume.setExperience(resumeDto.getExperience());
                    existingResume.setSkills(resumeDto.getSkills());
                    existingResume.setProjects(resumeDto.getProjects());
                    existingResume.setCertifications(resumeDto.getCertifications());
                    return resumeRepository.save(existingResume);
                })
                .orElseGet(() -> {
                    // CREATE new resume and link to profile
                    resumeDto.setJobSeeker(profile);
                    return resumeRepository.save(resumeDto);
                });
    }

    public Optional<Resume> getResumeByProfileId(Long id)
    {
        return resumeRepository.findByJobSeekerId(id);
    }

    public Optional<FileResume> getFileResumeById(Long id){
        return fileResumeRepository.findByJobSeekerId(id);
    }
}
