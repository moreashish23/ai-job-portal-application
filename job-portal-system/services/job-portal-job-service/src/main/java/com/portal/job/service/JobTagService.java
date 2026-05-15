package com.portal.job.service;

import com.portal.job.dto.response.JobTagResponse;
import com.portal.job.modal.JobTag;
import com.portal.job.payload.JobTagRequest;

import java.util.List;
import java.util.Set;

public interface JobTagService {

    JobTagResponse createTag(JobTagRequest req) throws Exception;

    List<JobTagResponse> getAllTags();

    JobTagResponse getById(Long id) throws Exception;

    JobTagResponse updateTag(Long id, JobTagRequest req) throws Exception;

    void deleteTag(Long id) throws Exception;

    JobTag getTagEntityById(Long id) throws Exception;

    Set<JobTag> getTagsByIds(Set<Long> ids) throws Exception;

}
