package com.portal.job.modal;

import com.portal.job.domain.JobType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "work_experiences")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Resume resume;

    @Column(nullable = false)
    private String companyName;

    private String companyLogoUrl;

    @Column(nullable = false)
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    private JobType employmentType;

    private String location;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isCurrentJob = false;

    private String description;

    @ElementCollection
    @CollectionTable(
            name = "work_experience_technologies",
            joinColumns = @JoinColumn(name = "work_experience_id")
    )
    @Column(name = "technology")
    private List<String> technologies = new ArrayList<>();

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
