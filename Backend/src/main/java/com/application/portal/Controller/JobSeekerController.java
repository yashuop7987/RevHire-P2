package com.application.portal.Controller;

import com.application.portal.Dto.JobSeekerDTO;
import com.application.portal.Model.*;
import com.application.portal.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200","http://localhost","http://localhost:80"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RestController
@RequestMapping("/jobseeker")
public class JobSeekerController {

     private final JobSeekerService jobSeekerService;
    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final ResumeService resumeService;

     @Autowired
     public JobSeekerController(JobSeekerService jobSeekerService, UserService userService, JobService jobService, ApplicationService applicationService, ResumeService resumeService)
     {
         this.jobSeekerService = jobSeekerService;
         this.userService=userService;
         this.jobService = jobService;
         this.applicationService = applicationService;
         this.resumeService = resumeService;
     }


    private String getLoggedInUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }



    @PostMapping("/profile")
    public ResponseEntity<?> createProfile(@RequestBody JobSeekerDTO jobSeekerProfileDTO)
    {

        try{
            JobSeeker jobSeekerProfile = new JobSeeker();

            jobSeekerProfile.setPhone(jobSeekerProfileDTO.getPhone());
            jobSeekerProfile.setEmail(jobSeekerProfileDTO.getEmail());
            jobSeekerProfile.setFullName(jobSeekerProfileDTO.getFullName());
            jobSeekerProfile.setLocation(jobSeekerProfileDTO.getLocation());
            jobSeekerProfile.setEmploymentStatus(jobSeekerProfileDTO.getEmploymentStatus());

            Optional<User> user = userService.getUserByUsername(getLoggedInUsername());

            jobSeekerProfile.setUser(user.get());

            jobSeekerService.createProfile(jobSeekerProfile);

            return ResponseEntity.status(HttpStatus.CREATED).build();



        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody JobSeekerDTO jobSeekerProfileDTO)
    {
        try{
            JobSeeker jobSeekerProfile = new JobSeeker();

            jobSeekerProfile.setEmail(jobSeekerProfileDTO.getEmail());
            jobSeekerProfile.setPhone(jobSeekerProfileDTO.getPhone());
            jobSeekerProfile.setLocation(jobSeekerProfileDTO.getLocation());
            jobSeekerProfile.setFullName(jobSeekerProfileDTO.getFullName());
            jobSeekerProfile.setEmploymentStatus(jobSeekerProfileDTO.getEmploymentStatus());


            jobSeekerService.updateProfie(jobSeekerProfile,getLoggedInUsername());

            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {

        String username = authentication.getName();

        try {
            Optional<JobSeeker> profile = jobSeekerService.getProfileByUsername(username);

            if(profile.isPresent()) {
                JobSeeker p = profile.get();

                return ResponseEntity.status(HttpStatus.OK).body(p);
            }

        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        return null;
    }





}
