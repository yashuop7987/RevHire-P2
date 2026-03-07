package com.application.portal.Controller;


import com.application.portal.Model.Job;
import com.application.portal.Service.SavedJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200","http://localhost","http://localhost:80"})
public class SavedJobController {

    @Autowired
    private SavedJobService savedJobService;

    @PostMapping("jobseeker/save-job/{jobId}/{seekerId}")
    public ResponseEntity<?> saveJob(@PathVariable Long jobId, @PathVariable Long seekerId) {
        try {
            savedJobService.saveJob(jobId, seekerId);
            return ResponseEntity.ok("Job saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("jobseeker/my-saved-jobs/{seekerId}")
    public ResponseEntity<List<Job>> getSavedJobs(@PathVariable Long seekerId) {
        return ResponseEntity.ok(savedJobService.getSavedJobsBySeeker(seekerId));
    }

    @GetMapping("jobseeker/is-saved/{jobId}/{seekerId}")
    public ResponseEntity<Boolean> isJobSaved(@PathVariable Long jobId, @PathVariable Long seekerId) {
        boolean saved = savedJobService.isAlreadySaved(jobId, seekerId);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("jobseeker/unsave-job/{jobId}/{seekerId}")
    public ResponseEntity<?> unsaveJob(@PathVariable Long jobId, @PathVariable Long seekerId) {
        try {
            savedJobService.removeSavedJob(jobId, seekerId);
            return ResponseEntity.ok("Job removed from saved list.");
        } catch (Exception e) {
            // Returns a 400 error if the job wasn't saved or ids are invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error removing job: " + e.getMessage());
        }
    }
}
