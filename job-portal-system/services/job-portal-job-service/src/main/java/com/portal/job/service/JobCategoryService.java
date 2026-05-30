package com.portal.job.service;

import com.portal.job.dto.response.JobCategoryResponse;
import com.portal.job.modal.JobCategory;
import com.portal.job.payload.JobCategoryRequest;

import java.util.List;


public interface JobCategoryService {

    JobCategoryResponse createCategory(JobCategoryRequest req)  ;

    List<JobCategoryResponse> getAllCategories();

    JobCategoryResponse getCategoryById(Long id)  ;

    JobCategoryResponse updateCategory(Long id, JobCategoryRequest req) ;

    void deleteCategory(Long id)  ;

    JobCategory getCategoryEntityById(Long id)  ;
}
