package com.portal.job.service;

import com.portal.job.dto.response.JobCategoryResponse;
import com.portal.job.modal.JobCategory;
import com.portal.job.payload.JobCategoryRequest;

import java.util.List;


public interface JobCategoryService {

    JobCategoryResponse createCategory(JobCategoryRequest req) throws Exception;

    List<JobCategoryResponse> getAllCategories();

    JobCategoryResponse getCategoryById(Long id) throws Exception;

    JobCategoryResponse updateCategory(Long id, JobCategoryRequest req) throws Exception;

    void deleteCategory(Long id) throws Exception;

    JobCategory getCategoryEntityById(Long id) throws Exception;
}
