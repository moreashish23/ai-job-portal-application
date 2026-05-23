package com.portal.job.service.impl;

import com.portal.job.exception.ForbiddenException;
import com.portal.job.exception.ResourceNotFoundException;
import com.portal.job.mapper.ApplicationMapper;
import com.portal.job.modal.ApplicationNote;
import com.portal.job.modal.JobApplication;
import com.portal.job.payload.AddNoteRequest;
import com.portal.job.payload.ApplicationNoteResponse;
import com.portal.job.repository.ApplicationNoteRepository;
import com.portal.job.repository.JobApplicationRepository;
import com.portal.job.service.ApplicationNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ApplicationNoteServiceImpl implements ApplicationNoteService {

    private final ApplicationNoteRepository noteRepository;
    private final JobApplicationRepository applicationRepository;

    @Override
    public ApplicationNoteResponse addNote(Long employerId, Long applicationId, AddNoteRequest request) {
        JobApplication application = getApplicationEntity(applicationId);
        assertEmployerOwner(application, employerId);

        ApplicationNote note = ApplicationNote.builder()
                .application(application)
                .authorId(employerId)
                .content(request.getContent())
                .build();

        ApplicationNote saved = noteRepository.save(note);
        log.info("Employer {} added note to application {}", employerId, applicationId);
        return ApplicationMapper.toNoteResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationNoteResponse> getNotesForApplication(Long employerId, Long applicationId) {
        JobApplication application = getApplicationEntity(applicationId);
        assertEmployerOwner(application, employerId);

        return noteRepository
                .findByApplication_IdOrderByCreatedAtDesc(applicationId)
                .stream()
                .map(ApplicationMapper::toNoteResponse)
                .toList();
    }

    @Override
    public void deleteNote(Long employerId, Long noteId) {
        ApplicationNote note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", noteId));

        if (!note.getAuthorId().equals(employerId)) {
            throw new ForbiddenException("You cannot delete this note.");
        }

        noteRepository.delete(note);
        log.info("Employer {} deleted note {}", employerId, noteId);
    }

    private JobApplication getApplicationEntity(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", applicationId));
    }

    private void assertEmployerOwner(JobApplication app, Long employerId) {
        if (!app.getEmployerId().equals(employerId)) {
            throw new ForbiddenException("You do not have access to this application.");
        }
    }
}