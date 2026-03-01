package com.application.portal.Repository;

import com.application.portal.Model.SavedJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJobs,Long> {
    List<SavedJobs> findByJobSeekerId(Long jobSeekerId);

    // Checks if a specific job is already saved by this seeker
    boolean existsByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);

    // Used for the "Unsave" functionality
    void deleteByJob_IdAndJobSeeker_Id(Long jobId, Long jobSeekerId);
}
