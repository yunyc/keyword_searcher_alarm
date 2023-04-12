package com.example.alarm.repository;

import com.example.alarm.domain.Notice;
import com.example.alarm.domain.SavedNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Notice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SavedNoticeRepository extends JpaRepository<SavedNotice, Long> {
    SavedNotice findByNoticeId(Long noticeId);
}
