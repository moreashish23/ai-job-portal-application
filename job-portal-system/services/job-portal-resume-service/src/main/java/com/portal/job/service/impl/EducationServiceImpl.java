package com.portal.job.service.impl;

import com.portal.job.dto.response.EducationResponse;
import com.portal.job.mapper.ResumeMapper;
import com.portal.job.modal.Education;
import com.portal.job.modal.Resume;
import com.portal.job.payload.AddEducationRequest;
import com.portal.job.repository.EducationRepository;
import com.portal.job.service.EducationService;
import com.portal.job.service.ResumeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final ResumeService resumeService;

    @Override
    public EducationResponse addEducation(Long resumeId, Long candidateId, AddEducationRequest req) throws Exception {
        Resume resume = resumeService.getResumeEntity(resumeId);
        assertOwner(resume, candidateId);

        Education education = Education.builder()
                .resume(resume)
                .institutionName(req.getInstitutionName())
                .degree(req.getDegree())
                .fieldOfStudy(req.getFieldOfStudy())
                .grade(req.getGrade())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .isCurrentlyStudying(
                        Boolean.TRUE.equals(req.getIsCurrentlyStudying())
                )
                .description(req.getDescription())
                .displayOrder(
                        req.getDisplayOrder() != null
                                ? req.getDisplayOrder()
                                : 0
                )
                .build();

        Education savedEducation = educationRepository.save(education);

        return ResumeMapper.toEducationResponse(savedEducation);
    }

    @Override
    public List<EducationResponse> getEducations(Long resumeId) {
        return educationRepository.findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toEducationResponse)
                .toList();
    }

    @Override
    public EducationResponse updateEducation(Long educationId, Long resumeId, Long candidateId, AddEducationRequest req) throws Exception {

        Education edu = educationRepository.findById(educationId)
                .orElseThrow(() -> new Exception("education does not exist"));

        assertOwner(edu.getResume(), candidateId);

        edu.setInstitutionName(req.getInstitutionName());
        edu.setDegree(req.getDegree());
        edu.setFieldOfStudy(req.getFieldOfStudy());
        edu.setGrade(req.getGrade());
        edu.setStartDate(req.getStartDate());
        edu.setEndDate(req.getEndDate());
        edu.setIsCurrentlyStudying(
                Boolean.TRUE.equals(req.getIsCurrentlyStudying())
        );
        edu.setDescription(req.getDescription());

        if (req.getDisplayOrder() != null) {
            edu.setDisplayOrder(req.getDisplayOrder());
        }

        return ResumeMapper.toEducationResponse(
                educationRepository.save(edu)    );
    }

    @Override
    public void deleteEducation(Long educationId, Long resumeId, Long candidateId) throws Exception {

        Education edu = educationRepository.findById(educationId)
                .orElseThrow(() -> new Exception("education does not exist"));

        assertOwner(edu.getResume(), candidateId);

        educationRepository.delete(edu);

    }

    private void assertOwner(Resume resume, Long candidateId) throws Exception {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new Exception(" Resume not found with id: " + candidateId);
        }
    }
}
