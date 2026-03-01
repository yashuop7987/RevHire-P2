package com.application.portal.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobSeekerDTO {
    private String fullName;
    private String phone;
    private String location;
    @Column(nullable = false,unique = true)
    private String email;
    private String employmentStatus;
}
