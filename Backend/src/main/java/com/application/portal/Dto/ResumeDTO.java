package com.application.portal.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDTO {
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
}
