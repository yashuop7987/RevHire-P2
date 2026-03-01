package com.application.portal.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String objective;

    @Column(length = 3000)
    private String education;

    @Column(length = 3000)
    private String experience;

    @Column(length = 2000)
    private String skills;

    @Column(length = 3000)
    private String projects;

    @Column(length = 2000)
    private String certifications;

    @OneToOne
    @JoinColumn(name = "jobseeker_id")
    private JobSeeker  jobSeeker;
}
