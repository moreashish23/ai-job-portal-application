package com.portal.job.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveJobRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;
}