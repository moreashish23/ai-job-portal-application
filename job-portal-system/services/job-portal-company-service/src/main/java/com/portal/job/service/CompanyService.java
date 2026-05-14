package com.portal.job.service;

import com.portal.job.domain.CompanyStatus;
import com.portal.job.domain.CompanyType;
import com.portal.job.domain.IndustryType;
import com.portal.job.dto.request.CompanyRequest;
import com.portal.job.dto.response.CompanyResponse;
import com.portal.job.modal.Company;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(Long ownerId, CompanyRequest req) throws Exception;

    CompanyResponse getCompanyById(Long id) throws Exception;

    CompanyResponse getMyCompany(Long ownerId) throws Exception;

    List<CompanyResponse> getAllCompanies(
            CompanyType companyType,
            IndustryType industryType,
            CompanyStatus companyStatus
    );

    CompanyResponse updateCompany(
            Long companyId,
            Long ownerId,
            CompanyRequest req
    ) throws Exception;

    CompanyResponse verifyCompany(Long companyId) throws Exception;

    void deleteCompany(Long companyId, Long ownerId) throws Exception;

    CompanyResponse deactivateCompany(Long companyId) throws Exception;

    Company getCompanyEntityById(Long id) throws Exception;

}
