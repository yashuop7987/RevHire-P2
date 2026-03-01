package com.application.portal.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;// EmployerStatsDTO.java
@Data
@AllArgsConstructor
public class EmployerStatsDTO {
    private long totalJobs;
    private long activeOpenings;
    private long totalApplications;
    private long pendingReviews;
}


