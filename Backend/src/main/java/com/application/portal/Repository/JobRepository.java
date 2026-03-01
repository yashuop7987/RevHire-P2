package com.application.portal.Repository;

import com.application.portal.Model.Employer;
import com.application.portal.Model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    List<Job> findByActiveTrue();

    List<Job> findByEmployer(Employer employer);
    List<Job> findByEmployerIdAndActive(Long employerId, boolean active);

    @Query("SELECT j FROM Job j WHERE " +
            "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:experience IS NULL OR j.experienceYears <= :experience) AND " +
            "(:jobType IS NULL OR j.jobType = :jobType)")
    List<Job> searchJobs(@Param("title") String title,
                         @Param("location") String location,
                         @Param("experience") Integer experience,
                         @Param("jobType") String jobType);

    long countByEmployer_Id(Long employerId);

    // 2. For "Active Openings" card: Counts only jobs with an 'ACTIVE' status
    // Assuming your Job entity has a status field
    long countByEmployer_IdAndActiveTrue(Long employerId);

    // 3. To list jobs in the "Manage Job Postings" section
    List<Job> findAllByEmployer_Id(Long employerId);
}
