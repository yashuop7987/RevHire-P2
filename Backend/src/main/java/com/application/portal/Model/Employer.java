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
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String industry;
    private String companySize;
    private String website;
    private String location;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(length = 2000)
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
