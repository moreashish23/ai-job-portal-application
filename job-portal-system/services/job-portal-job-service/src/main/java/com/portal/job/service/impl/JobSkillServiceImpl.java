package com.portal.job.service.impl;

import com.portal.job.dto.response.JobSkillResponse;
import com.portal.job.exception.BadRequestException;
import com.portal.job.exception.ResourceNotFoundException;
import com.portal.job.mapper.JobSkillMapper;
import com.portal.job.modal.JobSkill;
import com.portal.job.payload.JobSkillRequest;
import com.portal.job.repository.JobSkillRepository;
import com.portal.job.service.JobSkillService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobSkillServiceImpl implements JobSkillService {

    private final JobSkillRepository jobSkillRepository;

    @Override
    public JobSkillResponse createSkill(JobSkillRequest req)  {
        if(jobSkillRepository.existsByName(req.getName())) {
            throw new BadRequestException("Skill with name '"+req.getName()+"' already exists");
        }
        String slug = generateUniqueSlug(req.getName());
        JobSkill jobSkill = JobSkill.builder()
                .name(req.getName())
                .slug(slug)
                .category(req.getCategory())
                .active(true)
                .build();
        JobSkill savedSill =  jobSkillRepository.save(jobSkill);
        return JobSkillMapper.toJobSkillResponse(savedSill);
    }

    @Override
    public List<JobSkillResponse> getAllSkills() {
        return jobSkillRepository.findByActiveTrue()
                .stream().map(JobSkillMapper::toJobSkillResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JobSkillResponse getSkillById(Long id)   {
        JobSkill skill = jobSkillRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job Skill not found with ID: " + id)
        );
        return JobSkillMapper.toJobSkillResponse(skill);
    }

    @Override
    public JobSkillResponse updateSkill(Long id, JobSkillRequest req)  {
        JobSkill skill = jobSkillRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job Skill not found with ID: " + id)
        );

        if(!skill.getName().equals(req.getName())
        && jobSkillRepository.existsByName(skill.getName())) {
            throw new BadRequestException("Skill name already exist");
        }

        skill.setName(req.getName());
        skill.setCategory(req.getCategory());



        return JobSkillMapper.toJobSkillResponse(jobSkillRepository.save(skill));

    }

    @Override
    public void deleteSkill(Long id)   {
        JobSkill skill = jobSkillRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job Skill not found with ID: " + id)
        );
        skill.setActive(false);
        jobSkillRepository.save(skill);

    }

    @Override
    public Set<JobSkill> getSkillsByIds(Set<Long> ids) {
        Set<JobSkill> skills = new HashSet<>(jobSkillRepository.findAllById(ids));
        return skills;
    }

    private String generateUniqueSlug(@NotBlank(message = "company name is required") String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim().replaceAll("[\\s-]+", "-");

        if(!jobSkillRepository.existsBySlug(base)) {
            return base;
        }

        int counter=1;
        while(jobSkillRepository.existsBySlug(base+"-"+counter)) {
            counter++;
        }
        return base+"-"+counter;
    }
}
