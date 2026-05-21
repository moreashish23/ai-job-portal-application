package com.portal.job.modal;

import com.portal.job.domain.ResumeTemplate;
import com.portal.job.domain.ResumeVisibility;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long candidateId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResumeTemplate template = ResumeTemplate.PROFESSIONAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResumeVisibility visibility = ResumeVisibility.PUBLIC;

    @Column(nullable = false)
    private Boolean isDefault = false;

    @Embedded
    private PersonalInfo personalInfo;

    private String summary;

    private Integer completionScore=0;

    private Boolean isActive=true;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
