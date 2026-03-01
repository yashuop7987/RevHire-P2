package com.application.portal.Repository;


import com.application.portal.Model.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker,Long> {
    Optional<JobSeeker> findByUserUsername(String username);
}
