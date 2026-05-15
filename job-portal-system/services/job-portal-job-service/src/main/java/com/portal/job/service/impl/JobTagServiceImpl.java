package com.portal.job.service.impl;

import com.portal.job.dto.response.JobTagResponse;
import com.portal.job.mapper.JobTagMapper;
import com.portal.job.modal.JobTag;
import com.portal.job.payload.JobTagRequest;
import com.portal.job.repository.JobTagRepository;
import com.portal.job.service.JobTagService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobTagServiceImpl implements JobTagService {

    private final JobTagRepository jobTagRepository;

    @Override
    public JobTagResponse createTag(JobTagRequest req) throws Exception {

        if(jobTagRepository.existsByName(req.getName())) {
            throw new Exception("Tag with name '"+req.getName()+"' already exists");
        }

        String slug=generateUniqueSlug(req.getName());

        JobTag jobTag= JobTag.builder()
                .name(req.getName())
                .slug(slug)
                .build();

        JobTag savedJobTag=jobTagRepository.save(jobTag);
        return JobTagMapper.toTagResponse(savedJobTag);
    }

    @Override
    public List<JobTagResponse> getAllTags() {
        return jobTagRepository.findAll()
                .stream().map(JobTagMapper::toTagResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JobTagResponse getById(Long id) throws Exception {
        JobTag  jobTag= getTagEntityById(id);

        return JobTagMapper.toTagResponse(jobTag);

    }

    @Override
    public JobTagResponse updateTag(Long id, JobTagRequest req) throws Exception {
        JobTag  jobTag= getTagEntityById(id);

        if(!jobTag.getName().equals(req.getName()) &&
                jobTagRepository.existsByName(req.getName())) {
            throw new Exception("Tag with name '"+req.getName()+"' already exists");
        }
        jobTag.setName(req.getName());
        return JobTagMapper.toTagResponse(jobTagRepository.save(jobTag));
    }

    @Override
    public void deleteTag(Long id) throws Exception {

        JobTag  jobTag= getTagEntityById(id);
        jobTagRepository.delete(jobTag);

    }


    @Override
    public JobTag getTagEntityById(Long id) throws Exception {
        return jobTagRepository.findById(id).orElseThrow(
                () -> new Exception("Job tag not found with ID: " + id)
        );
    }

    @Override
    public Set<JobTag> getTagsByIds(Set<Long> ids) throws Exception {
        List<JobTag> tags= jobTagRepository.findAllById(ids);
        return new HashSet<>(tags);
    }

    private String generateUniqueSlug(@NotBlank(message = "company name is required") String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim().replaceAll("[\\s-]+", "-");

        if(!jobTagRepository.existsBySlug(base)) {
            return base;
        }

        int counter=1;
        while(jobTagRepository.existsBySlug(base+"-"+counter)) {
            counter++;
        }
        return base+"-"+counter;
    }
}
