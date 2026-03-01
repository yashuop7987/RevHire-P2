package com.application.portal.Service;

import com.application.portal.Dto.EmployerDTO;
import com.application.portal.Model.Employer;
import com.application.portal.Model.User;
import com.application.portal.Repository.EmployerRepository;
import com.application.portal.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployerService {


    private final EmployerRepository employerRepository;

    private final UserRepository userRepository;

    @Autowired
    public EmployerService(EmployerRepository employerRepository, UserRepository userRepository) {
        this.employerRepository = employerRepository;
        this.userRepository = userRepository;
    }



    public Employer createProfile(Employer profile)
    {

        return employerRepository.save(profile);
    }

   public Optional<Employer> getProfileByUsername(String username)
   {
       return employerRepository.findByUserUsername(username);
   }

    @Transactional
    public Employer saveOrUpdateProfile(EmployerDTO dto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employer employer = employerRepository
                .findByUser_Id(user.getId())
                .orElse(new Employer());

        employer.setCompanyName(dto.getCompanyName());
        employer.setIndustry(dto.getIndustry());
        employer.setCompanySize(dto.getCompanySize());
        employer.setWebsite(dto.getWebsite());
        employer.setLocation(dto.getLocation());
        employer.setEmail(dto.getEmail());
        employer.setDescription(dto.getDescription());

        employer.setUser(user); // Always ensure relation

        return employerRepository.save(employer);
    }

}

