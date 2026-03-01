package com.application.portal.Service;

import com.application.portal.Model.JobSeeker;
import com.application.portal.Repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerService {

    private JobSeekerRepository jobSeekerRepository;


    @Autowired
    public JobSeekerService(JobSeekerRepository jobSeekerRepository)
    {
        this.jobSeekerRepository= jobSeekerRepository;
    }


    public JobSeeker createProfile(JobSeeker jobSeekerProfile)
    {
        return jobSeekerRepository.save(jobSeekerProfile);
    }

    public Optional<JobSeeker> getProfileByUsername(String username)
    {
        return jobSeekerRepository.findByUserUsername(username);
    }

    public JobSeeker updateProfie(JobSeeker jobSeekerProfile,String username)
    {
      Optional<JobSeeker> jobSeekerProfile1= jobSeekerRepository.findByUserUsername(username);
      JobSeeker jobSeekerProfile2 = jobSeekerProfile1.get();

      jobSeekerProfile2.setPhone(jobSeekerProfile.getPhone());
      jobSeekerProfile2.setEmail(jobSeekerProfile.getEmail());
      jobSeekerProfile2.setLocation(jobSeekerProfile.getLocation());
      jobSeekerProfile2.setEmploymentStatus(jobSeekerProfile.getEmploymentStatus());
      jobSeekerProfile2.setFullName(jobSeekerProfile.getFullName());

      return jobSeekerRepository.save(jobSeekerProfile2);
    }

}
