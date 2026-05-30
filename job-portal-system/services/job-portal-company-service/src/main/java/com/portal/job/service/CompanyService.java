package com.portal.job.service;

import com.portal.job.domain.CompanyStatus;
import com.portal.job.domain.CompanyType;
import com.portal.job.domain.IndustryType;
import com.portal.job.dto.request.CompanyRequest;
import com.portal.job.dto.response.CompanyResponse;
import com.portal.job.modal.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {

    CompanyResponse createCompany(Long ownerId, CompanyRequest req) ;

    CompanyResponse getCompanyById(Long id) ;

    CompanyResponse getMyCompany(Long ownerId) ;

    Page<CompanyResponse> getAllCompanies(
            CompanyType companyType,
            IndustryType industryType,
            CompanyStatus companyStatus,
            Pageable pageable
    );

    CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest req) ;

    CompanyResponse verifyCompany(Long companyId) ;

    void deleteCompany(Long companyId, Long ownerId) ;
    CompanyResponse deactivateCompany(Long companyId) ;

    Company getCompanyEntityById(Long id) ;
}