package com.portal.job.repository;

import com.portal.job.modal.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    List<Language> findByResume_IdOrderByDisplayOrderAsc(Long resume_id);

}
