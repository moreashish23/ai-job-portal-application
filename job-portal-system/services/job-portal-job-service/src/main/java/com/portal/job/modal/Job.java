package com.portal.job.modal;

import com.portal.job.domain.ExperienceLevel;
import com.portal.job.domain.JobStatus;
import com.portal.job.domain.JobType;
import com.portal.job.domain.WorkMode;
import com.portal.job.modal.embeddable.JobLocation;
import com.portal.job.modal.embeddable.SalaryRange;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String requirements;

    private String responsibilities;

    private String benefits;

    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private Long employerId;

    @ManyToOne
     private JobCategory category;

     @ManyToMany
     private Set<JobSkill> skills;

        @ManyToMany
     private Set<JobTag> tags;

    @Embedded
    private JobLocation location;

    @Embedded
    private SalaryRange salaryRange;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status=JobStatus.DRAFT;

    private Integer openings=1;

    private LocalDate applicationDeadline;

    private LocalDate expiresAt;

    private Boolean active=true;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;
    private LocalDateTime closedAt;



}
