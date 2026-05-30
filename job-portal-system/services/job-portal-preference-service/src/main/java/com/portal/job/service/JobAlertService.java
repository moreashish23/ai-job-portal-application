package com.portal.job.service;

import com.portal.job.payload.CreateJobAlertRequest;
import com.portal.job.payload.JobAlertResponse;
import com.portal.job.payload.UpdateJobAlertRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobAlertService {

    JobAlertResponse createAlert(Long candidateId, CreateJobAlertRequest request);

    Page<JobAlertResponse> getMyAlerts(Long candidateId, Pageable pageable);

    JobAlertResponse getAlertById(Long candidateId, Long alertId);

    JobAlertResponse updateAlert(Long candidateId, Long alertId, UpdateJobAlertRequest request);

    JobAlertResponse toggleAlert(Long candidateId, Long alertId);

    void deleteAlert(Long candidateId, Long alertId);
}