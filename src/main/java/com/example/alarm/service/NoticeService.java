package com.example.alarm.service;

import com.example.alarm.domain.event.NoticeChanged;
import com.example.alarm.web.rest.dto.NoticeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.example.alarm.domain.Notice}.
 */
public interface NoticeService {
    /**
     * Save a notice.
     *
     * @param noticeDTO the entity to save.
     * @return the persisted entity.
     */
    NoticeDTO save(NoticeDTO noticeDTO);

    /**
     * Updates a notice.
     *
     * @param noticeDTO the entity to update.
     * @return the persisted entity.
     */
    NoticeDTO update(NoticeDTO noticeDTO);

    /**
     * Partially updates a notice.
     *
     * @param noticeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NoticeDTO> partialUpdate(NoticeDTO noticeDTO);

    /**
     * Get all the notices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NoticeDTO> findAll(Pageable pageable);

    Page<NoticeDTO> findAllByUserId(Pageable pageable, String userId, Boolean visiable);

    /**
     * Get the "id" notice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NoticeDTO> findOne(Long id);

    /**
     * Delete the "id" notice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void processNoticeChanged(NoticeChanged noticeChanged);
}
