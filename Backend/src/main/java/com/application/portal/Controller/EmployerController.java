package com.application.portal.Controller;

import com.application.portal.Dto.EmployerDTO;
import com.application.portal.Model.Employer;
import com.application.portal.Repository.JobRepository;
import com.application.portal.Service.EmployerService;
import com.application.portal.Service.JobService;
import com.application.portal.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200","http://localhost","http://localhost:80"})
@RestController
@RequestMapping("/employer")
public class EmployerController {




    private final JobService jobService;
    private final EmployerService employerProfileService;
    private final UserService userService;
    private final JobRepository jobRepository;


    @Autowired
    public EmployerController(JobService jobService, EmployerService employerProfileService, UserService userService, JobRepository jobRepository) {
        this.jobService = jobService;
        this.employerProfileService = employerProfileService;
        this.userService = userService;
        this.jobRepository = jobRepository;
    }


    private String getLoggedInUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @PutMapping("/profile")
    public ResponseEntity<Employer> updateProfile(
            @RequestBody EmployerDTO dto,
            Authentication authentication) {

        String username = authentication.getName();

        Employer savedEmployer =
                employerProfileService.saveOrUpdateProfile(dto, username);

        return ResponseEntity.ok(savedEmployer);
    }






    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {

        String username = authentication.getName();

        try {
            Optional<Employer> profile = employerProfileService.getProfileByUsername(username);

            if(profile.isPresent()) {
                Employer p = profile.get();

                return ResponseEntity.status(HttpStatus.OK).body(p);
            }

        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        return null;
    }




}
