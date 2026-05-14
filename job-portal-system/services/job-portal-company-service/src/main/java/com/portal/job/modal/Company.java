package com.portal.job.modal;

import com.portal.job.domain.CompanySize;
import com.portal.job.domain.CompanyStatus;
import com.portal.job.domain.CompanyType;
import com.portal.job.domain.IndustryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true,  nullable = false)
    private String name;

    @Column(unique = true)
    private String slug;

    private String tagline;

    private String description;

    private String logoUrl;

    private String coverImageUrl;
    private String website;

    private String email;
    private String phone;

    private Integer foundedYear;

    @Enumerated(EnumType.STRING)
    private CompanySize companySize;

    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Enumerated(EnumType.STRING)
    private IndustryType industryType;

    private CompanyStatus status;

    private boolean isVerified=false;

    @Column(unique = true)
    private String registrationNumber;

    @Column(unique = true, nullable = false)
    private Long ownerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<SocialLink> socialLinks = new ArrayList<>();

    private Boolean active = true;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
