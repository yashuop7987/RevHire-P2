package com.application.portal.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDTO {
    private String title;
    private String description;
    private String skills;
    private Integer experienceYears;
    private String education;
    private String location;
    private Double salary;
    private String jobType;
    private LocalDateTime deadline;
    private boolean active = true;
}
