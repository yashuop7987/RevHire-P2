package com.application.portal.Repository;

import com.application.portal.Model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long> {

    Optional<Resume> findByJobSeekerId(Long jobSeekerId);
}
