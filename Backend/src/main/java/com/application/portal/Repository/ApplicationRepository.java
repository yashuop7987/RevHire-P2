package com.application.portal.Repository;

import com.application.portal.Model.Application;
import com.application.portal.Model.ApplicationStatus;
import com.application.portal.Model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {

    List<Application> findByJobEmployer(Employer employerProfile);
    boolean existsByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);
    List<Application> findByJobSeekerId(Long jobSeekerId);
    List<Application> findAllByJob_Employer_Id(Long employerId);

    // If you want to fetch applications by a specific status for an employer
    List<Application> findAllByJob_Employer_IdAndStatus(Long employerId, ApplicationStatus status);

    long countByJob_Employer_Id(Long employerId);

    // 5. For "Pending Reviews" card: Counts apps where status is 'APPLIED'
    long countByJob_Employer_IdAndStatus(Long employerId, ApplicationStatus status);

}
