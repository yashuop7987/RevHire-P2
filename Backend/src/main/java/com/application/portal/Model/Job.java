package com.application.portal.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 3000)
    private String description;

    private String skills;
    private Integer experienceYears;
    private String education;
    private String location;
    private Double salary;
    private String jobType;
    private LocalDateTime deadline;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

    }

}
