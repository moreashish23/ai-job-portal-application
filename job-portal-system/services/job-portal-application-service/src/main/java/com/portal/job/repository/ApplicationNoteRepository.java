package com.portal.job.repository;

import com.portal.job.modal.ApplicationNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationNoteRepository extends JpaRepository<ApplicationNote, Long> {

    List<ApplicationNote> findByApplication_IdOrderByCreatedAtDesc(Long applicationId);
}