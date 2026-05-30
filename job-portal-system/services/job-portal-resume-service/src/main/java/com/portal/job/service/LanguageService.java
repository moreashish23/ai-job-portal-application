package com.portal.job.service;

import com.portal.job.dto.response.LanguageResponse;
import com.portal.job.payload.AddLanguageRequest;


import java.util.List;

public interface LanguageService {

    LanguageResponse addLanguage(
            Long resumeId,
            Long candidateId,
            AddLanguageRequest req
    ) ;

    List<LanguageResponse> getLanguages(Long resumeId);

    LanguageResponse updateLanguage(
            Long languageId,
            Long resumeId,
            Long candidateId,
            AddLanguageRequest req
    ) ;

    void deleteLanguage(
            Long languageId,
            Long resumeId,
            Long candidateId
    ) ;
}
