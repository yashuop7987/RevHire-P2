package com.application.portal.Controller;

import com.application.portal.Model.FileResume;
import com.application.portal.Model.Resume;
import com.application.portal.Repository.FileResumeRepository;
import com.application.portal.Service.ResumeService;
import io.swagger.v3.oas.models.Paths;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.Optional;

@RestController
public class ResumeController {

    private ResumeService resumeService;

    private FileResumeRepository fileResumeRepository;

    @Autowired
    public ResumeController(ResumeService resumeService,FileResumeRepository fileResumeRepository)
    {
        this.resumeService=resumeService;
        this.fileResumeRepository=fileResumeRepository;
    }

    @PostMapping("/jobseeker/save/{profileId}")
    public ResponseEntity<Resume> handleResume(@PathVariable Long profileId, @RequestBody Resume resume) {
        Resume savedResume = resumeService.saveOrUpdateResume(profileId, resume);
        return ResponseEntity.ok(savedResume);
    }

    @GetMapping("/jobseeker/my-resume/{profileId}")
    public ResponseEntity<Resume> getResume(@PathVariable Long profileId) {
        return resumeService.getResumeByProfileId(profileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/resume/download/{id}")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Long id) {
        // Use .get() or handle Optional correctly
        FileResume fileResume = fileResumeRepository.findByJobSeekerId(id)
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResume.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(fileResume.getFileType()))
                .body(fileResume.getData());
    }
}
