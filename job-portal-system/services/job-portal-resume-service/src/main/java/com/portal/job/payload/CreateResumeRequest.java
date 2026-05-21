package com.portal.job.payload;

import com.portal.job.domain.ResumeTemplate;
import com.portal.job.domain.ResumeVisibility;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateResumeRequest {

    @NotBlank(message = "Resume title is required")
    private String title;

    private ResumeTemplate template;

    private ResumeVisibility visibility;

    private Boolean isDefault;

}