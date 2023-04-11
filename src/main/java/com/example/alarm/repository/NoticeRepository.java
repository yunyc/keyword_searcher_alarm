package com.example.alarm.repository;

import com.example.alarm.domain.Notice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Notice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {}
