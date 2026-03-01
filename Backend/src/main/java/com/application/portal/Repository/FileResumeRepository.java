package com.application.portal.Repository;


import com.application.portal.Model.FileResume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileResumeRepository extends JpaRepository<FileResume,Long> {
    Optional<FileResume> findByJobSeekerId(Long jobSeekerId);


}
