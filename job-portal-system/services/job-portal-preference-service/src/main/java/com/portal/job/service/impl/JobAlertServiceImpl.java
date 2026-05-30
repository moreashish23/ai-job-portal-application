package com.portal.job.service.impl;

import com.portal.job.exception.BadRequestException;
import com.portal.job.exception.ForbiddenException;
import com.portal.job.exception.ResourceNotFoundException;
import com.portal.job.mapper.PreferenceMapper;
import com.portal.job.modal.JobAlert;
import com.portal.job.payload.CreateJobAlertRequest;
import com.portal.job.payload.JobAlertResponse;
import com.portal.job.payload.UpdateJobAlertRequest;
import com.portal.job.repository.JobAlertRepository;
import com.portal.job.service.JobAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class JobAlertServiceImpl implements JobAlertService {

    private final JobAlertRepository jobAlertRepository;

    private static final int MAX_ACTIVE_ALERTS = 20;

    @Override
    @Transactional
    public JobAlertResponse createAlert(Long candidateId, CreateJobAlertRequest request) {

        long activeAlerts = jobAlertRepository.countByCandidateIdAndIsActiveTrue(candidateId);
        if (activeAlerts >= MAX_ACTIVE_ALERTS) {
            throw new BadRequestException(
                    "Maximum active alert limit reached (" + MAX_ACTIVE_ALERTS + "). "
                            + "Please disable or delete existing alerts before creating new ones.");
        }

        JobAlert alert = PreferenceMapper.toEntity(candidateId, request);
        JobAlert saved = jobAlertRepository.save(alert);

        log.info("Candidate {} created job alert '{}' (id: {})", candidateId, request.getAlertName(), saved.getId());
        return PreferenceMapper.toAlertResponse(saved);
    }

    @Override
    public Page<JobAlertResponse> getMyAlerts(Long candidateId, Pageable pageable) {
        return jobAlertRepository
                .findByCandidateIdOrderByCreatedAtDesc(candidateId, pageable)
                .map(PreferenceMapper::toAlertResponse);
    }

    @Override
    public JobAlertResponse getAlertById(Long candidateId, Long alertId) {
        JobAlert alert = getAlertEntity(alertId, candidateId);
        return PreferenceMapper.toAlertResponse(alert);
    }

    @Override
    @Transactional
    public JobAlertResponse updateAlert(Long candidateId, Long alertId, UpdateJobAlertRequest request) {
        JobAlert alert = getAlertEntity(alertId, candidateId);
        PreferenceMapper.applyUpdate(alert, request);
        JobAlert updated = jobAlertRepository.save(alert);
        log.info("Candidate {} updated alert {}", candidateId, alertId);
        return PreferenceMapper.toAlertResponse(updated);
    }

    @Override
    @Transactional
    public JobAlertResponse toggleAlert(Long candidateId, Long alertId) {
        JobAlert alert = getAlertEntity(alertId, candidateId);

        boolean willActivate = !alert.getIsActive();
        if (willActivate) {
            long activeAlerts = jobAlertRepository.countByCandidateIdAndIsActiveTrue(candidateId);
            if (activeAlerts >= MAX_ACTIVE_ALERTS) {
                throw new BadRequestException(
                        "Cannot activate — maximum active alert limit of " + MAX_ACTIVE_ALERTS + " reached.");
            }
        }

        alert.setIsActive(willActivate);
        JobAlert updated = jobAlertRepository.save(alert);
        log.info("Candidate {} {} alert {}", candidateId, willActivate ? "activated" : "deactivated", alertId);
        return PreferenceMapper.toAlertResponse(updated);
    }

    @Override
    @Transactional
    public void deleteAlert(Long candidateId, Long alertId) {
        JobAlert alert = getAlertEntity(alertId, candidateId);
        jobAlertRepository.delete(alert);
        log.info("Candidate {} deleted alert {}", candidateId, alertId);
    }

    // ─── Private helpers ───────────────────────────────────────────────────────

    private JobAlert getAlertEntity(Long alertId, Long candidateId) {
        return jobAlertRepository.findByIdAndCandidateId(alertId, candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Job alert", alertId));
    }
}