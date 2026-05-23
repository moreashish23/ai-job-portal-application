package com.portal.job.repository;


import com.portal.job.domain.JobStatus;
import com.portal.job.modal.Job;
import com.portal.job.payload.JobSearchRequest;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.portal.job.modal.JobSkill;
import com.portal.job.modal.JobTag;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    private JobSpecification() {}

    public static Specification<Job> build(JobSearchRequest req) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isTrue(root.get("active")));

            JobStatus status =
                    req.getStatus() != null
                            ? req.getStatus()
                            : JobStatus.OPEN;

            predicates.add(cb.equal(root.get("status"), status));

            if (req.getJobType() != null) {
                predicates.add(
                        cb.equal(root.get("jobType"), req.getJobType())
                );
            }

            if (req.getWorkMode() != null) {
                predicates.add(
                        cb.equal(root.get("workMode"), req.getWorkMode())
                );
            }

            if (req.getExperienceLevel() != null) {
                predicates.add(
                        cb.equal(
                                root.get("experienceLevel"),
                                req.getExperienceLevel()
                        )
                );
            }

            if (req.getCompanyId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("companyId"),
                                req.getCompanyId()
                        )
                );
            }

            if (req.getCategoryId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("category").get("id"),
                                req.getCategoryId()
                        )
                );
            }

            // location filter
            if (req.getLocation() != null &&
                    !req.getLocation().isBlank()) {

                String pattern =
                        "%" + req.getLocation().toLowerCase() + "%";

                Path<String> city =
                        root.get("location").get("city");

                Path<String> state =
                        root.get("location").get("state");

                Path<String> country =
                        root.get("location").get("country");

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(city), pattern),
                                cb.like(cb.lower(state), pattern),
                                cb.like(cb.lower(country), pattern)
                        )
                );
            }

            // salary filter
            if (req.getMinSalary() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("salaryRange")
                                        .get("maxSalary"),
                                req.getMinSalary()
                        )
                );
            }

            if (req.getMaxSalary() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("salaryRange")
                                        .get("minSalary"),
                                req.getMaxSalary()
                        )
                );
            }

            // openings range
            if (req.getMinOpenings() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("openings"),
                                req.getMinOpenings()
                        )
                );
            }

            if (req.getMaxOpenings() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("openings"),
                                req.getMaxOpenings()
                        )
                );
            }

            // keyword search (title + description)
            if (req.getKeyword() != null && !req.getKeyword().isBlank()) {
                String pattern = "%" + req.getKeyword().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                ));
            }

// skill filter (job must have AT LEAST ONE of the requested skills)
            if (req.getSkillIds() != null && !req.getSkillIds().isEmpty()) {
                Join<Job, JobSkill> skillJoin = root.join("skills", JoinType.INNER);
                predicates.add(skillJoin.get("id").in(req.getSkillIds()));
                query.distinct(true);
            }

// tag filter
            if (req.getTagIds() != null && !req.getTagIds().isEmpty()) {
                Join<Job, JobTag> tagJoin = root.join("tags", JoinType.INNER);
                predicates.add(tagJoin.get("id").in(req.getTagIds()));
                query.distinct(true);
            }

            // TODO : filter for tags, skills

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
