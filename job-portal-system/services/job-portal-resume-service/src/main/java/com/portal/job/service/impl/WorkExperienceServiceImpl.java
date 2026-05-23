package com.portal.job.service.impl;

import com.portal.job.dto.response.WorkExperienceResponse;
import com.portal.job.mapper.WorkExperienceMapper;
import com.portal.job.modal.Resume;
import com.portal.job.modal.WorkExperience;
import com.portal.job.payload.AddWorkExperience;
import com.portal.job.repository.WorkExperienceRepository;
import com.portal.job.service.ResumeService;
import com.portal.job.service.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkExperienceServiceImpl implements WorkExperienceService {

    private final ResumeService resumeService;

    private final WorkExperienceRepository workExperienceRepository;

    @Override
    public WorkExperienceResponse addWorkExperience(Long resumeId, Long candidateId,
                                                    AddWorkExperience req) throws Exception {

        Resume resume = resumeService.getResumeEntity(resumeId);

        assertOwner(resume, candidateId);

        WorkExperience workExperience = WorkExperience.builder()
                .resume(resume)
                .companyName(req.getCompanyName())
                .companyLogoUrl(req.getCompanyLogoUrl())
                .jobTitle(req.getJobTitle())
                .employmentType(req.getEmploymentType())
                .location(req.getLocation())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .isCurrentJob(Boolean.TRUE.equals(req.getIsCurrentJob()))
                .description(req.getDescription())
                .technologies(
                        req.getTechnologies() != null
                                ? req.getTechnologies()
                                : List.of()
                )
                .displayOrder(
                        req.getDisplayOrder() != null
                                ? req.getDisplayOrder()
                                : 0
                )
                .build();

        WorkExperience saved = workExperienceRepository.save(workExperience);

        return WorkExperienceMapper.toWorkExperienceResponse(saved);
    }


    @Override
    public List<WorkExperienceResponse> getWorkExperiences(Long resumeId) {
        return workExperienceRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(WorkExperienceMapper::toWorkExperienceResponse)
                .toList();
    }

    @Override
    public WorkExperienceResponse updateWorkExperience(Long resumeId, Long candidateId,
                                                       Long workExperienceId,
                                                       AddWorkExperience req) throws Exception {
        WorkExperience exp = getWorkExperienceEntity(workExperienceId);
        assertOwner(exp.getResume(), candidateId);

        exp.setCompanyName(req.getCompanyName());
        exp.setCompanyLogoUrl(req.getCompanyLogoUrl());
        exp.setJobTitle(req.getJobTitle());
        exp.setEmploymentType(req.getEmploymentType());
        exp.setLocation(req.getLocation());
        exp.setStartDate(req.getStartDate());
        exp.setEndDate(req.getEndDate());
        exp.setIsCurrentJob(Boolean.TRUE.equals(req.getIsCurrentJob()));
        exp.setDescription(req.getDescription());

        if (req.getTechnologies() != null) {
            exp.setTechnologies(req.getTechnologies());
        }

        if (req.getDisplayOrder() != null) {
            exp.setDisplayOrder(req.getDisplayOrder());
        }

        WorkExperience updated = workExperienceRepository.save(exp);

        return WorkExperienceMapper.toWorkExperienceResponse(updated);
    }

    @Override
    public void deleteWorkExperience(Long resumeId, Long workExperienceId, Long candidateId) throws Exception {

        WorkExperience exp = getWorkExperienceEntity(workExperienceId);
         assertOwner(exp.getResume(), candidateId);
        workExperienceRepository.delete(exp);

    }

    @Override
    public WorkExperience getWorkExperienceEntity(Long workExperienceId) throws Exception {
        return workExperienceRepository.findById(workExperienceId)
                .orElseThrow(
                        () -> new Exception("Work experience not found with id: " + workExperienceId)
                );
    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new Exception(" Resume not found with id: ");
        }
    }
}
