package com.portal.job.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiTextResponse {

    private String result;
    private boolean success;
    private String model;
}