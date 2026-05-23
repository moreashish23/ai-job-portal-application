package com.portal.job.service;

import com.portal.job.dto.response.ProjectResponse;
import com.portal.job.payload.AddProjectRequest;

import java.util.List;

public interface ProjectService {

    ProjectResponse addProject(
            Long resumeId,
            Long candidateId,
            AddProjectRequest req
    ) throws Exception;

    List<ProjectResponse> getAllProjects(Long resumeId);

    ProjectResponse updateProject(
            Long projectId,
            Long resumeId,
            Long candidateId,
            AddProjectRequest req
    ) throws Exception;

    void deleteProject(
            Long projectId,
            Long resumeId,
            Long candidateId
    ) throws Exception;
}