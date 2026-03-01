package com.application.portal.Repository;

import com.application.portal.Model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployerRepository extends JpaRepository<Employer,Long> {

    Optional<Employer> findByUserUsername(String username);
    Optional<Employer> findByEmail(String email);
    Optional<Employer> findByUser_Id(Long userId);

    // Optional: Find by company name for search functionality
    Optional<Employer> findByCompanyName(String companyName);


}
