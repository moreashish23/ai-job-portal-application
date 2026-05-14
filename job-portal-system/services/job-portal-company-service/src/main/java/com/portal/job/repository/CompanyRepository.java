package com.portal.job.repository;

import com.portal.job.domain.CompanyStatus;
import com.portal.job.domain.CompanyType;
import com.portal.job.domain.IndustryType;
import com.portal.job.modal.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByOwnerId(Long ownerId);

    boolean existsByOwnerId(Long ownerId);

    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    boolean existsByRegistrationNumber(String registrationNumber);

    @Query(
            "select c from Company c where " +
                    "(:companyType IS NULL OR c.companyType = :companyType) AND " +
                    "(:industryType IS NULL OR c.industryType = :industryType) AND " +
                    "(:status IS NULL OR c.status = :status)"
    )
    List<Company> findByFilters(
            @Param("companyType") CompanyType companyType,
            @Param("industryType") IndustryType industryType,
            @Param("status") CompanyStatus status
    );

}
