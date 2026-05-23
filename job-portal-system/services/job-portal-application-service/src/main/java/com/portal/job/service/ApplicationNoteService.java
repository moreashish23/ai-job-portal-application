package com.portal.job.service;

import com.portal.job.payload.AddNoteRequest;
import com.portal.job.payload.ApplicationNoteResponse;

import java.util.List;

public interface ApplicationNoteService {

    ApplicationNoteResponse addNote(Long employerId, Long applicationId, AddNoteRequest request);

    List<ApplicationNoteResponse> getNotesForApplication(Long employerId, Long applicationId);

    void deleteNote(Long employerId, Long noteId);
}