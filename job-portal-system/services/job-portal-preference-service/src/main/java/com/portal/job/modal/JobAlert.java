package com.portal.job.modal;

import com.portal.job.domain.ExperienceLevel;
import com.portal.job.domain.JobType;
import com.portal.job.domain.WorkMode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "job_alerts",
        indexes = {
                @Index(name = "idx_alert_candidate", columnList = "candidate_id"),
                @Index(name = "idx_alert_active", columnList = "is_active")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    // Human-readable name the candidate gives the alert
    @Column(nullable = false, length = 100)
    private String alertName;

    // Search criteria — all optional, combined with AND when present
    private String keyword;

    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    private Long categoryId;

    private BigDecimal minSalary;
    private BigDecimal maxSalary;

    // Alert delivery frequency
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertFrequency frequency = AlertFrequency.DAILY;

    // Whether the alert is actively sending emails
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Tracks when the alert last triggered (used by the notification scheduler)
    private LocalDateTime lastTriggeredAt;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}