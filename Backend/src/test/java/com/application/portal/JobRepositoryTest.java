package com.application.portal.Repository;

import com.application.portal.Model.Employer;
import com.application.portal.Model.Job;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TestEntityManager entityManager;

    // 🔹 Helper Method: Create Employer
    private Employer createEmployer(String companyName) {
        Employer employer = new Employer();
        employer.setCompanyName(companyName);
        return entityManager.persistAndFlush(employer);
    }

    // 🔹 Helper Method: Create Job
    private Job createJob(String title, String location, int experience,
                          boolean active, Employer employer) {

        Job job = Job.builder()
                .title(title)
                .description("Sample Description")
                .skills("Java, Spring Boot")
                .experienceYears(experience)
                .education("B.Tech")
                .location(location)
                .salary(800000.0)
                .jobType("FULL_TIME")
                .deadline(LocalDateTime.now().plusDays(30))
                .active(active)
                .employer(employer)
                .build();

        return entityManager.persistAndFlush(job);
    }

    // ✅ Test findByActiveTrue()
    @Test
    @DisplayName("Should return only active jobs")
    void testFindByActiveTrue() {

        Employer emp = createEmployer("TechNova");

        createJob("Java Dev", "Pune", 2, true, emp);
        createJob("Angular Dev", "Mumbai", 3, false, emp);

        List<Job> activeJobs = jobRepository.findByActiveTrue();

        assertThat(activeJobs).hasSize(1);
        assertThat(activeJobs.get(0).isActive()).isTrue();
    }

    // ✅ Test findByEmployerIdAndActive()
    @Test
    @DisplayName("Should return active jobs for specific employer")
    void testFindByEmployerIdAndActive() {

        Employer emp = createEmployer("NextGen");

        createJob("Backend Dev", "Hyderabad", 4, true, emp);
        createJob("Frontend Dev", "Bangalore", 2, false, emp);

        List<Job> jobs =
                jobRepository.findByEmployerIdAndActive(emp.getId(), true);

        assertThat(jobs).hasSize(1);
        assertThat(jobs.get(0).getTitle()).isEqualTo("Backend Dev");
    }

    // ✅ Test searchJobs()
    @Test
    @DisplayName("Should search jobs by title and location")
    void testSearchJobs() {

        Employer emp = createEmployer("CodeCraft");

        createJob("Java Developer", "Pune", 2, true, emp);
        createJob("Angular Developer", "Mumbai", 3, true, emp);

        List<Job> result = jobRepository.searchJobs(
                "Java",   // title
                "Pune",   // location
                null,     // experience
                null      // jobType
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("Java");
    }

    // ✅ Test countByEmployer_Id()
    @Test
    @DisplayName("Should count jobs by employer id")
    void testCountByEmployerId() {

        Employer emp = createEmployer("InnovateTech");

        createJob("DevOps Engineer", "Pune", 3, true, emp);
        createJob("QA Engineer", "Pune", 2, true, emp);

        long count = jobRepository.countByEmployer_Id(emp.getId());

        assertThat(count).isEqualTo(2);
    }

    // ✅ Test countByEmployer_IdAndActiveTrue()
    @Test
    @DisplayName("Should count only active jobs for employer")
    void testCountByEmployerIdAndActiveTrue() {

        Employer emp = createEmployer("StartupHub");

        createJob("Job 1", "Delhi", 2, true, emp);
        createJob("Job 2", "Delhi", 3, false, emp);

        long count =
                jobRepository.countByEmployer_IdAndActiveTrue(emp.getId());

        assertThat(count).isEqualTo(1);
    }
}