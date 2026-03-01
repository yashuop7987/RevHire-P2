package com.application.portal.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerDTO {
    private Long userId;
    private String companyName;
    private String industry;
    private String companySize;
    private String website;
    private String location;
    private String email;
    private String description;
}
