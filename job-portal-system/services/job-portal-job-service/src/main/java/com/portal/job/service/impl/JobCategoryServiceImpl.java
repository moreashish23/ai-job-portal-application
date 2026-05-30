package com.portal.job.service.impl;

import com.portal.job.dto.response.JobCategoryResponse;
import com.portal.job.exception.BadRequestException;
import com.portal.job.exception.ResourceNotFoundException;
import com.portal.job.mapper.JobCategoryMapper;
import com.portal.job.modal.JobCategory;
import com.portal.job.payload.JobCategoryRequest;
import com.portal.job.repository.JobCategoryRepository;
import com.portal.job.service.JobCategoryService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobCategoryServiceImpl implements JobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    @Override
    public JobCategoryResponse createCategory(JobCategoryRequest req)  {

        if(jobCategoryRepository.existsByName(req.getName())) {
            throw new BadRequestException("Category with name already exists");
        }

       JobCategory parent = null;
        if(req.getParentId() != null) {
            parent = getCategoryEntityById(req.getParentId());
        }

        String slug = generateUniqueSlug(req.getName());

        JobCategory category = JobCategory.builder()
                .name(req.getName())
                .description(req.getDescription())
                .slug(slug)
                .iconUrl(req.getIconUrl())
                .parent(parent)
                .active(true)
                .build();

        JobCategory saved = jobCategoryRepository.save(category);

        return JobCategoryMapper.toJobCategoryResponse(saved, true);

    }

    @Override
    public List<JobCategoryResponse> getAllCategories() {
        return jobCategoryRepository.findByActiveTrue().stream()
                .map(c -> JobCategoryMapper.toJobCategoryResponse(c, false))
                .collect(Collectors.toList());
    }

    @Override
    public JobCategoryResponse getCategoryById(Long id)   {
        JobCategory jobCategory = getCategoryEntityById(id);
        return JobCategoryMapper.toJobCategoryResponse(jobCategory, true);
    }

    @Override
    public JobCategoryResponse updateCategory(Long id, JobCategoryRequest req)   {

        JobCategory category = getCategoryEntityById(id);

        if(!category.getName().equals(req.getName()) &&
        jobCategoryRepository.existsByName(req.getName())) {
            throw new BadRequestException("Category with name already exists, choose different name");
        }

        JobCategory parent = null;
        if(req.getParentId()!=null) {
            if(req.getParentId().equals(id)) {
                throw new BadRequestException("Category cannot be parent of itself");
            }
            parent = getCategoryEntityById(req.getParentId());
        }

        category.setName(req.getName());
        category.setDescription(req.getDescription());
        category.setIconUrl(req.getIconUrl());
        category.setParent(parent);

        JobCategory saved = jobCategoryRepository.save(category);
        return JobCategoryMapper.toJobCategoryResponse(saved, true);
    }

    @Override
    public void deleteCategory(Long id)   {

        JobCategory category = getCategoryEntityById(id);
        category.setActive(false);
        jobCategoryRepository.save(category);

    }

    @Override
    public JobCategory getCategoryEntityById(Long id)  {
        return jobCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found")
        );
    }

    private String generateUniqueSlug(@NotBlank(message = "company name is required") String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim().replaceAll("[\\s-]+", "-");

        if(!jobCategoryRepository.existsBySlug(base)) {
            return base;
        }

        int counter=1;
        while(jobCategoryRepository.existsBySlug(base+"-"+counter)) {
            counter++;
        }
        return base+"-"+counter;
    }


}
