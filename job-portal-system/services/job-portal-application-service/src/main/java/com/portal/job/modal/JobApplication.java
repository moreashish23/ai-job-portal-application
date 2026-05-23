package com.portal.job.modal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "job_applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_candidate_job",
                        columnNames = {"candidate_id", "job_id"}
                )
        },
        indexes = {
                @Index(name = "idx_application_candidate", columnList = "candidate_id"),
                @Index(name = "idx_application_job", columnList = "job_id"),
                @Index(name = "idx_application_employer", columnList = "employer_id"),
                @Index(name = "idx_application_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "employer_id", nullable = false)
    private Long employerId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "resume_id", nullable = false)
    private Long resumeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    // AI screening score (0-100), set after AI processing
    private Integer aiScore;

    // AI-generated screening summary
    @Column(columnDefinition = "TEXT")
    private String aiScreeningSummary;

    // Employer-facing: candidate has been viewed
    @Column(nullable = false)
    private Boolean viewed = false;

    // Employer-facing: candidate has been shortlisted
    @Column(nullable = false)
    private Boolean shortlisted = false;

    // Optional: answers to employer custom questions
    @Column(columnDefinition = "TEXT")
    private String additionalAnswers;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime appliedAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}