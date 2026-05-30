package com.portal.job.service.impl;

import com.portal.job.domain.CompanyStatus;
import com.portal.job.domain.CompanyType;
import com.portal.job.domain.IndustryType;
import com.portal.job.dto.request.CompanyRequest;
import com.portal.job.dto.response.CompanyResponse;
import com.portal.job.dto.response.SocialLinkResponse;
import com.portal.job.exception.BadRequestException;
import com.portal.job.exception.ForbiddenException;
import com.portal.job.exception.ResourceNotFoundException;
import com.portal.job.mapper.CompanyMapper;
import com.portal.job.modal.Company;
import com.portal.job.modal.SocialLink;
import com.portal.job.repository.CompanyRepository;
import com.portal.job.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;



    @Override
    public CompanyResponse createCompany(Long ownerId, CompanyRequest req) {

        if (companyRepository.existsByOwnerId(ownerId)) {
            throw new BadRequestException(
                    "You already have a company registered. Only one company per account is allowed.");
        }

        if (companyRepository.existsByName(req.getName())) {
            throw new BadRequestException(
                    "A company with this name already exists. Please choose a different name.");
        }

        if (req.getRegistrationNumber() != null &&
                companyRepository.existsByRegistrationNumber(req.getRegistrationNumber())) {
            throw new BadRequestException(
                    "A company with this registration number already exists.");
        }

        String slug = generateUniqueSlug(req.getName());

        Company company = Company.builder()
                .name(req.getName())
                .slug(slug)
                .tagline(req.getTagline())
                .description(req.getDescription())
                .logoUrl(req.getLogoUrl())
                .coverImageUrl(req.getCoverImageUrl())
                .website(req.getWebsite())
                .email(req.getEmail())
                .phone(req.getPhone())
                .foundedYear(req.getFoundedYear())
                .companySize(req.getCompanySize())
                .companyType(req.getCompanyType())
                .industryType(req.getIndustryType())
                .registrationNumber(req.getRegistrationNumber())
                .ownerId(ownerId)
                .socialLinks(mapSocialLinks(req.getSocialLinks()))
                .build();

        try {
            Company saved = companyRepository.save(company);
            return CompanyMapper.toResponse(saved);
        } catch (DataIntegrityViolationException e) {
            // Catches race condition: two concurrent requests with same name/slug
            throw new BadRequestException(
                    "A company with this name or registration number already exists.");
        }
    }

    // ─── Read ────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", id));
        return CompanyMapper.toResponse(company);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getMyCompany(Long ownerId) {
        Company company = companyRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No company found for this account."));
        return CompanyMapper.toResponse(company);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyResponse> getAllCompanies(
            CompanyType companyType,
            IndustryType industryType,
            CompanyStatus companyStatus,
            Pageable pageable) {

        return companyRepository
                .findByFilters(companyType, industryType, companyStatus, pageable)
                .map(CompanyMapper::toResponse);
    }


    @Override
    public CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest req) {
        Company company = getCompanyEntityById(companyId);
        assertOwner(company, ownerId);

        if (!company.getName().equals(req.getName())
                && companyRepository.existsByName(req.getName())) {
            throw new BadRequestException(
                    "A company with this name already exists. Please choose a different name.");
        }

        if (req.getRegistrationNumber() != null
                && !req.getRegistrationNumber().equals(company.getRegistrationNumber())
                && companyRepository.existsByRegistrationNumber(req.getRegistrationNumber())) {
            throw new BadRequestException(
                    "A company with this registration number already exists.");
        }

        company.setName(req.getName());
        company.setTagline(req.getTagline());
        company.setDescription(req.getDescription());
        company.setLogoUrl(req.getLogoUrl());
        company.setCoverImageUrl(req.getCoverImageUrl());
        company.setWebsite(req.getWebsite());
        company.setEmail(req.getEmail());
        company.setPhone(req.getPhone());
        company.setFoundedYear(req.getFoundedYear());
        company.setCompanySize(req.getCompanySize());
        company.setCompanyType(req.getCompanyType());
        company.setIndustryType(req.getIndustryType());
        company.setRegistrationNumber(req.getRegistrationNumber());
        company.setSocialLinks(mapSocialLinks(req.getSocialLinks()));

        return CompanyMapper.toResponse(companyRepository.save(company));
    }


    @Override
    public CompanyResponse verifyCompany(Long companyId) {
        Company company = getCompanyEntityById(companyId);
        company.setStatus(CompanyStatus.ACTIVE);
        company.setVerified(true);
        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public CompanyResponse deactivateCompany(Long companyId) {
        Company company = getCompanyEntityById(companyId);
        company.setStatus(CompanyStatus.SUSPENDED);
        company.setVerified(false);
        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public void deleteCompany(Long companyId, Long ownerId) {
        Company company = getCompanyEntityById(companyId);
        assertOwner(company, ownerId);
        companyRepository.delete(company);
    }



    @Override
    @Transactional(readOnly = true)
    public Company getCompanyEntityById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", id));
    }


    private void assertOwner(Company company, Long ownerId) {
        if (!company.getOwnerId().equals(ownerId)) {
            throw new ForbiddenException("You do not have permission to modify this company.");
        }
    }

    private List<SocialLink> mapSocialLinks(List<SocialLinkResponse> socialLinks) {
        if (socialLinks == null || socialLinks.isEmpty()) {
            return new ArrayList<>();
        }
        return socialLinks.stream()
                .map(e -> SocialLink.builder()
                        .platform(e.getPlatform())
                        .url(e.getUrl())
                        .build())
                .toList();
    }

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("[\\s-]+", "-");

        if (!companyRepository.existsBySlug(base)) {
            return base;
        }

        int counter = 1;
        while (companyRepository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }
}