package com.portal.job.service;

import com.portal.job.payload.request.*;
import com.portal.job.payload.response.*;

public interface AiService {

    AiTextResponse generateJobDescription(GenerateJobDescriptionRequest request);

    AiTextResponse generateCoverLetter(GenerateCoverLetterRequest request);

    CandidateScoreResponse scoreCandidate(ScoreCandidateRequest request);

    AiTextResponse optimizeResume(OptimizeResumeRequest request);

    JobSearchQueryResponse parseNaturalLanguageSearch(NaturalLanguageJobSearchRequest request);
}