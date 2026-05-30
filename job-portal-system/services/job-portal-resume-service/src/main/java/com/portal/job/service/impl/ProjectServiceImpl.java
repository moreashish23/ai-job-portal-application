package com.portal.job.service.impl;

import com.portal.job.dto.response.ProjectResponse;
import com.portal.job.exception.ForbiddenException;
import com.portal.job.exception.ResourceNotFoundException;
import com.portal.job.mapper.ResumeMapper;
import com.portal.job.modal.Project;
import com.portal.job.modal.Resume;
import com.portal.job.payload.AddProjectRequest;
import com.portal.job.repository.ProjectRepository;
import com.portal.job.service.ProjectService;
import com.portal.job.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ResumeService resumeService;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectResponse addProject(Long resumeId, Long candidateId, AddProjectRequest req) {
        Resume resume = resumeService.getResumeEntity(resumeId);

        assertOwner(resume, candidateId);

        Project project = Project.builder()
                .resume(resume)
                .title(req.getTitle())
                .description(req.getDescription())
                .technologies(
                        req.getTechnologies() != null
                                ? req.getTechnologies()
                                : List.of()
                )
                .projectUrl(req.getProjectUrl())
                .sourceCodeUrl(req.getSourceCodeUrl())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .isOngoing(Boolean.TRUE.equals(req.getIsOngoing()))
                .displayOrder(
                        req.getDisplayOrder() != null
                                ? req.getDisplayOrder()
                                : 0
                )
                .build();

        Project savedProject = projectRepository.save(project);
        return ResumeMapper.toProjectResponse(savedProject);
    }

    @Override
    public List<ProjectResponse> getAllProjects(Long resumeId) {
        return projectRepository
                .findByResume_IdOrderByDisplayOrderAsc(resumeId)
                .stream()
                .map(ResumeMapper::toProjectResponse)
                .toList();
    }

    @Override
    public ProjectResponse updateProject(Long projectId, Long resumeId,
                                         Long candidateId, AddProjectRequest req) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("project not found"));

        assertOwner(project.getResume(), candidateId);

        project.setTitle(req.getTitle());
        project.setDescription(req.getDescription());

        if (req.getTechnologies() != null) {
            project.setTechnologies(req.getTechnologies());
        }

        project.setProjectUrl(req.getProjectUrl());
        project.setSourceCodeUrl(req.getSourceCodeUrl());
        project.setStartDate(req.getStartDate());
        project.setEndDate(req.getEndDate());
        project.setIsOngoing(Boolean.TRUE.equals(req.getIsOngoing()));

        if (req.getDisplayOrder() != null) {
            project.setDisplayOrder(req.getDisplayOrder());
        }

        return ResumeMapper.toProjectResponse(
                projectRepository.save(project)
        );
    }

    @Override
    public void deleteProject(Long projectId, Long resumeId, Long candidateId)   {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("project not found"));

        assertOwner(project.getResume(), candidateId);

        projectRepository.delete(project);
    }

    private void assertOwner(Resume resume, Long candidateId) {
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new ForbiddenException("You do not have access to this resume.");
        }
    }
}
