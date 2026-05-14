package com.portal.job.mapper;

import com.portal.job.dto.response.CompanyResponse;
import com.portal.job.dto.response.SocialLinkResponse;
import com.portal.job.modal.Company;
import com.portal.job.modal.SocialLink;

import java.util.Collections;
import java.util.List;

public class CompanyMapper {

    public static SocialLinkResponse toSocialLinkResponse(SocialLink socialLinks) {

        return SocialLinkResponse.builder()
                .platform(socialLinks.getPlatform())
                .url(socialLinks.getUrl())
                .build();
    }

    public static CompanyResponse toResponse(Company company) {

        List<SocialLinkResponse> socialLinks =
                company.getSocialLinks() == null
                        ? Collections.emptyList()
                        : company.getSocialLinks().stream()
                        .map(CompanyMapper::toSocialLinkResponse)
                        .toList();

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .slug(company.getSlug())
                .tagline(company.getTagline())
                .description(company.getDescription())
                .logoUrl(company.getLogoUrl())
                .coverImageUrl(company.getCoverImageUrl())
                .website(company.getWebsite())
                .email(company.getEmail())
                .phone(company.getPhone())
                .foundedYear(company.getFoundedYear())
                .companySize(company.getCompanySize())
                .companyType(company.getCompanyType())
                .industryType(company.getIndustryType())
                .status(company.getStatus())
                .active(company.getActive())
                .ownerId(company.getOwnerId())
                .socialLinks(socialLinks)
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

}
