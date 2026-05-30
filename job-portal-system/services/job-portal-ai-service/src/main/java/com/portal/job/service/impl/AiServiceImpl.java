package com.portal.job.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.job.client.GroqApiClient;
import com.portal.job.exception.AiServiceException;
import com.portal.job.payload.request.*;
import com.portal.job.payload.response.*;
import com.portal.job.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {

    private final GroqApiClient groqApiClient;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.model}")
    private String model;

    // ── 1. Generate Job Description ────────────────────────────────────────────

    @Override
    public AiTextResponse generateJobDescription(GenerateJobDescriptionRequest request) {

        String systemPrompt = """
                You are an expert HR professional and technical recruiter with 15 years of experience.
                Your task is to write compelling, accurate, and professional job descriptions.
                Always structure your output with clear sections:
                Overview, Responsibilities, Requirements, Nice to Have, and Benefits.
                Write in second person ("You will...", "You have...").
                Be specific, avoid corporate jargon, and keep the tone professional yet engaging.
                Do NOT include salary information unless provided.
                Return ONLY the job description text — no extra commentary.
                """;

        String userPrompt = buildJobDescriptionPrompt(request);

        String result = groqApiClient.complete(systemPrompt, userPrompt);

        return AiTextResponse.builder()
                .result(result)
                .success(true)
                .model(model)
                .build();
    }

    // ── 2. Generate Cover Letter ───────────────────────────────────────────────

    @Override
    public AiTextResponse generateCoverLetter(GenerateCoverLetterRequest request) {

        String systemPrompt = """
                You are a professional career coach and expert cover letter writer.
                Write compelling, personalized cover letters that highlight the candidate's
                relevant experience and genuine interest in the role.
                Structure: Opening paragraph, Skills/Experience paragraph, Closing paragraph.
                Tone should be confident, genuine, and tailored — not generic.
                Keep it to 3 paragraphs maximum. Do NOT use placeholder text like [Your Name].
                Return ONLY the cover letter text — no extra commentary or titles.
                """;

        String userPrompt = buildCoverLetterPrompt(request);

        String result = groqApiClient.complete(systemPrompt, userPrompt);

        return AiTextResponse.builder()
                .result(result)
                .success(true)
                .model(model)
                .build();
    }

    // ── 3. Score Candidate ────────────────────────────────────────────────────

    @Override
    public CandidateScoreResponse scoreCandidate(ScoreCandidateRequest request) {

        String systemPrompt = """
                You are an expert technical recruiter and hiring manager.
                Evaluate how well a candidate matches a job opening.
                You MUST respond with ONLY a valid JSON object — no explanation, no markdown, no code blocks.
                The JSON must have exactly these fields:
                {
                  "score": <integer 0-100>,
                  "summary": "<2-3 sentence evaluation>",
                  "matchedSkills": "<comma-separated list of matched skills>",
                  "missingSkills": "<comma-separated list of missing skills>",
                  "recommendation": "<one of: STRONG_MATCH, GOOD_MATCH, PARTIAL_MATCH, NOT_A_MATCH>"
                }
                """;

        String userPrompt = buildScoringPrompt(request);

        String rawResponse = groqApiClient.complete(systemPrompt, userPrompt);

        return parseScoringResponse(rawResponse, request.getApplicationId());
    }

    // ── 4. Optimize Resume ────────────────────────────────────────────────────

    @Override
    public AiTextResponse optimizeResume(OptimizeResumeRequest request) {

        String systemPrompt = """
                You are a professional resume coach and career advisor.
                Your task is to improve a candidate's resume summary/profile section.
                Make it ATS-friendly, keyword-rich, and compelling to hiring managers.
                Improve clarity, remove clichés, strengthen action verbs, and ensure it
                highlights measurable impact where possible.
                If a target job is provided, tailor the summary toward that role.
                Return ONLY the improved resume summary text — no titles, no commentary.
                """;

        String userPrompt = buildResumeOptimizationPrompt(request);

        String result = groqApiClient.complete(systemPrompt, userPrompt);

        return AiTextResponse.builder()
                .result(result)
                .success(true)
                .model(model)
                .build();
    }

    // ── 5. Natural Language Job Search ────────────────────────────────────────

    @Override
    public JobSearchQueryResponse parseNaturalLanguageSearch(NaturalLanguageJobSearchRequest request) {

        String systemPrompt = """
                You are a search query parser for a job portal.
                Extract structured search parameters from a natural language job search query.
                You MUST respond with ONLY a valid JSON object — no explanation, no markdown, no code blocks.
                The JSON must have exactly these fields (use null if not determinable):
                {
                  "keyword": "<key skills or job title keywords>",
                  "location": "<city, state, or country if mentioned, else null>",
                  "jobType": "<one of: FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, FREELANCE, REMOTE, or null>",
                  "workMode": "<one of: REMOTE, HYBRID, ON_SITE, or null>",
                  "experienceLevel": "<one of: ENTRY, JUNIOR, MID, SENIOR, LEAD, or null>"
                }
                """;

        String userPrompt = "Parse this job search query: \"" + request.getQuery() + "\"";

        String rawResponse = groqApiClient.complete(systemPrompt, userPrompt);

        return parseSearchQueryResponse(rawResponse, request.getQuery());
    }

    // ── Prompt builders ───────────────────────────────────────────────────────

    private String buildJobDescriptionPrompt(GenerateJobDescriptionRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("Write a complete job description for the following role:\n\n");
        sb.append("Job Title: ").append(req.getJobTitle()).append("\n");

        if (req.getCompanyName() != null && !req.getCompanyName().isBlank()) {
            sb.append("Company: ").append(req.getCompanyName()).append("\n");
        }
        if (req.getJobType() != null) {
            sb.append("Employment Type: ").append(req.getJobType()).append("\n");
        }
        if (req.getWorkMode() != null) {
            sb.append("Work Mode: ").append(req.getWorkMode()).append("\n");
        }
        if (req.getExperienceLevel() != null) {
            sb.append("Experience Level: ").append(req.getExperienceLevel()).append("\n");
        }
        if (req.getRequiredSkills() != null && !req.getRequiredSkills().isEmpty()) {
            sb.append("Required Skills: ").append(String.join(", ", req.getRequiredSkills())).append("\n");
        }
        if (req.getAdditionalContext() != null && !req.getAdditionalContext().isBlank()) {
            sb.append("Additional Context: ").append(req.getAdditionalContext()).append("\n");
        }

        return sb.toString();
    }

    private String buildCoverLetterPrompt(GenerateCoverLetterRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("Write a cover letter for this candidate:\n\n");
        sb.append("Candidate Name: ").append(req.getCandidateName()).append("\n");
        sb.append("Applying For: ").append(req.getJobTitle()).append(" at ").append(req.getCompanyName()).append("\n");
        sb.append("Experience Summary: ").append(req.getCandidateExperienceSummary()).append("\n");

        if (req.getCandidateSkills() != null && !req.getCandidateSkills().isEmpty()) {
            sb.append("Key Skills: ").append(String.join(", ", req.getCandidateSkills())).append("\n");
        }
        if (req.getTone() != null && !req.getTone().isBlank()) {
            sb.append("Desired Tone: ").append(req.getTone()).append("\n");
        }

        return sb.toString();
    }

    private String buildScoringPrompt(ScoreCandidateRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("Evaluate this candidate for the following job opening:\n\n");
        sb.append("=== JOB ===\n");
        sb.append("Title: ").append(req.getJobTitle()).append("\n");
        sb.append("Experience Required: ").append(req.getExperienceLevel()).append("\n");
        sb.append("Job Description: ").append(req.getJobDescription()).append("\n");

        if (req.getJobRequiredSkills() != null && !req.getJobRequiredSkills().isEmpty()) {
            sb.append("Required Skills: ").append(String.join(", ", req.getJobRequiredSkills())).append("\n");
        }

        sb.append("\n=== CANDIDATE ===\n");
        sb.append("Experience Summary: ").append(req.getCandidateExperienceSummary()).append("\n");

        if (req.getCandidateSkills() != null && !req.getCandidateSkills().isEmpty()) {
            sb.append("Skills: ").append(String.join(", ", req.getCandidateSkills())).append("\n");
        }
        if (req.getCoverLetter() != null && !req.getCoverLetter().isBlank()) {
            sb.append("Cover Letter: ").append(req.getCoverLetter()).append("\n");
        }

        return sb.toString();
    }

    private String buildResumeOptimizationPrompt(OptimizeResumeRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("Improve the following resume summary/profile section:\n\n");
        sb.append("Current Summary:\n").append(req.getCurrentSummary()).append("\n\n");

        if (req.getCurrentSkills() != null && !req.getCurrentSkills().isEmpty()) {
            sb.append("Candidate Skills: ").append(String.join(", ", req.getCurrentSkills())).append("\n");
        }
        if (req.getTargetJobTitle() != null && !req.getTargetJobTitle().isBlank()) {
            sb.append("Target Role: ").append(req.getTargetJobTitle()).append("\n");
        }
        if (req.getTargetExperienceLevel() != null && !req.getTargetExperienceLevel().isBlank()) {
            sb.append("Target Level: ").append(req.getTargetExperienceLevel()).append("\n");
        }

        return sb.toString();
    }

    // ── JSON response parsers ─────────────────────────────────────────────────

    private CandidateScoreResponse parseScoringResponse(String rawJson, Long applicationId) {
        // Strip markdown code blocks if Groq wraps the JSON despite instructions
        String cleaned = cleanJsonResponse(rawJson);

        try {
            JsonNode node = objectMapper.readTree(cleaned);

            return CandidateScoreResponse.builder()
                    .applicationId(applicationId)
                    .score(node.path("score").asInt(0))
                    .summary(node.path("summary").asText(""))
                    .matchedSkills(node.path("matchedSkills").asText(""))
                    .missingSkills(node.path("missingSkills").asText(""))
                    .recommendation(node.path("recommendation").asText("NOT_A_MATCH"))
                    .success(true)
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Failed to parse candidate scoring JSON response: {}", e.getMessage());
            log.debug("Raw Groq response was: {}", rawJson);
            throw new AiServiceException("AI returned an invalid scoring response. Please retry.");
        }
    }

    private JobSearchQueryResponse parseSearchQueryResponse(String rawJson, String originalQuery) {
        String cleaned = cleanJsonResponse(rawJson);

        try {
            JsonNode node = objectMapper.readTree(cleaned);

            return JobSearchQueryResponse.builder()
                    .originalQuery(originalQuery)
                    .keyword(nullIfBlank(node.path("keyword").asText(null)))
                    .location(nullIfBlank(node.path("location").asText(null)))
                    .jobType(nullIfBlank(node.path("jobType").asText(null)))
                    .workMode(nullIfBlank(node.path("workMode").asText(null)))
                    .experienceLevel(nullIfBlank(node.path("experienceLevel").asText(null)))
                    .success(true)
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Failed to parse search query JSON response: {}", e.getMessage());
            // Graceful fallback — use the full query as keyword, no structure
            return JobSearchQueryResponse.builder()
                    .originalQuery(originalQuery)
                    .keyword(originalQuery)
                    .success(false)
                    .build();
        }
    }

    private String cleanJsonResponse(String raw) {
        if (raw == null) return "{}";
        // Remove markdown code fences that LLMs sometimes add despite instructions
        return raw.replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
    }

    private String nullIfBlank(String value) {
        if (value == null || value.isBlank() || value.equalsIgnoreCase("null")) {
            return null;
        }
        return value;
    }
}