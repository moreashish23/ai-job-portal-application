package com.portal.job.service.impl;

import com.portal.job.domain.JobStatus;
import com.portal.job.dto.request.JobRequest;
import com.portal.job.dto.response.CompanyResponse;
import com.portal.job.dto.response.JobResponse;
import com.portal.job.mapper.JobMapper;
import com.portal.job.modal.Job;
import com.portal.job.modal.JobCategory;
import com.portal.job.modal.JobSkill;
import com.portal.job.modal.JobTag;
import com.portal.job.modal.embeddable.JobLocation;
import com.portal.job.modal.embeddable.SalaryRange;
import com.portal.job.payload.JobSearchRequest;
import com.portal.job.repository.JobRepository;
import com.portal.job.repository.JobSpecification;
import com.portal.job.service.JobCategoryService;
import com.portal.job.service.JobService;
import com.portal.job.service.JobSkillService;
import com.portal.job.service.JobTagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobCategoryService categoryService;
    private final JobSkillService skillService;
    private final JobTagService tagService;

    @Override
    public JobResponse createJob(Long employerId, JobRequest req) throws Exception {

        JobCategory category = categoryService.getCategoryEntityById(req.getCategoryId());

        Set<JobSkill> skills = req.getSkillIds()!=null ?
                skillService.getSkillsByIds(req.getSkillIds()) :
                Collections.emptySet();

        Set<JobTag> tags = req.getTagIds()!=null ?
                tagService.getTagsByIds(req.getTagIds()) :
                Collections.emptySet();


        Long companyId= 1L;

        Job job = Job.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .requirements(req.getRequirements())
                .responsibilities(req.getResponsibilities())
                .benefits(req.getBenefits())
                .companyId(companyId)
                .employerId(employerId)
                .category(category)
                .skills(skills)
                .tags(tags)
                .location(buildLocation(req))
                .salaryRange(buildSalaryRange(req))
                .jobType(req.getJobType())
                .workMode(req.getWorkMode())
                .experienceLevel(req.getExperienceLevel())
                .openings(req.getOpenings() != null ? req.getOpenings() : 1)
                .applicationDeadline(req.getApplicationDeadline())
                .expiresAt(req.getExpiresAt())
                .active(true)
                .status(JobStatus.DRAFT)
                .build();

        Job savedJob = jobRepository.save(job);

        return convertToResponse(savedJob);
    }



    @Override
    public JobResponse getJobById(Long id) throws Exception {

        Job job = jobRepository.findById(id).orElseThrow(
                () -> new Exception("Job not Found with " + id)
        );

        return convertToResponse(job);
    }

    @Override
    public List<JobResponse> getJobs(JobSearchRequest request) {

        List<Job> jobs = jobRepository.findAll(JobSpecification.build(request));
        return jobs.stream().map(
                this::convertToResponse
        ).collect(Collectors.toList());
    }

    @Override
    public List<JobResponse> getJobsByCompany(Long companyId) {

        List<Job> jobs = jobRepository.findByCompanyId(companyId);
        return jobs.stream().map(
                this::convertToResponse
        ).collect(Collectors.toList());

    }

    @Override
    public JobResponse updateJob(Long jobId, Long employerId, JobRequest req) throws Exception {

        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new Exception("Job not found with id: " + jobId)
        );

        assertEmployer(job, employerId);

        JobCategory category = categoryService.getCategoryEntityById(req.getCategoryId());

        Set<JobSkill> skills = req.getSkillIds()!=null ?
                skillService.getSkillsByIds(req.getSkillIds()) :
                Collections.emptySet();

        Set<JobTag> tags = req.getTagIds()!=null ?
                tagService.getTagsByIds(req.getTagIds()) :
                Collections.emptySet();

            job.setTitle(req.getTitle());
            job.setDescription(req.getDescription());
            job.setRequirements(req.getRequirements());
            job.setResponsibilities(req.getResponsibilities());
            job.setBenefits(req.getBenefits());
            job.setCategory(category);
            job.setSkills(skills);
            job.setTags(tags);
            job.setLocation(buildLocation(req));
            job.setSalaryRange(buildSalaryRange(req));
            job.setJobType(req.getJobType());
            job.setWorkMode(req.getWorkMode());
            job.setExperienceLevel(req.getExperienceLevel());
            job.setOpenings(req.getOpenings() != null ? req.getOpenings() : 1);
            job.setApplicationDeadline(req.getApplicationDeadline());
            job.setExpiresAt(req.getExpiresAt());

        return convertToResponse(jobRepository.save(job));
    }

    @Override
    public JobResponse publishJob(Long jobId, Long employerId) throws Exception {

        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new Exception("Job not found with id: " + jobId)
        );

        assertEmployer(job, employerId);
        if(job.getStatus() == JobStatus.CLOSED || job.getStatus()==JobStatus.EXPIRED){
            throw new Exception("Job is expired");
        }
        job.setStatus(JobStatus.OPEN);
        job.setPublishedAt(java.time.LocalDateTime.now());
        job.setActive(true);

        return convertToResponse(jobRepository.save(job));
    }

    @Override
    public JobResponse closeJob(Long jobId, Long employerId) throws Exception {

        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new Exception("Job not found with id: " + jobId)
        );

        assertEmployer(job, employerId);
        job.setStatus(JobStatus.CLOSED);
        job.setClosedAt(java.time.LocalDateTime.now());
        job.setActive(false);

        return convertToResponse(jobRepository.save(job));
    }

    @Override
    public void deleteJob(Long jobId, Long employerId) throws Exception {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new Exception("Job not found with id: " + jobId)
        );

        assertEmployer(job, employerId);
        jobRepository.delete(job);
    }

    @Override
    public List<JobResponse> getAllJobsAdmin() {
        return jobRepository.findAll().stream().map(
                this::convertToResponse
        ).collect(Collectors.toList());
    }


    private JobResponse convertToResponse(Job savedJob) {

        CompanyResponse companyResponse = CompanyResponse.builder()
                .id(savedJob.getCompanyId())
                .build();
        return JobMapper.toResponse(savedJob, companyResponse);
    }

    private SalaryRange buildSalaryRange(JobRequest req) {

        return SalaryRange.builder()
                .minSalary(req.getMinSalary())
                .maxSalary(req.getMaxSalary())
                .build();
    }

    private JobLocation buildLocation(JobRequest req) {
        return JobLocation.builder()
                .address(req.getAddress())
                .city(req.getCity())
                .state(req.getState())
                .country(req.getCountry())
                .zipCode(req.getZipCode())
                .build();
    }

    private void assertEmployer(Job job, Long employerId) throws Exception {
        if (!job.getEmployerId().equals(employerId)) {
            throw new Exception("Unauthorized: You are not the employer who posted this job");
        }
    }
}
