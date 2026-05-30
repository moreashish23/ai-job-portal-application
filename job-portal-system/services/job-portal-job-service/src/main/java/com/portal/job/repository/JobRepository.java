package com.portal.job.repository;

import com.portal.job.modal.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    Page<Job> findByCompanyId(Long companyId, Pageable pageable);

    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.category WHERE j.id = :id")
    Optional<Job> findByIdWithCategory(@Param("id") Long id);

}
