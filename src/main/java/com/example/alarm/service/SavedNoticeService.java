package com.example.alarm.service;

import com.example.alarm.web.rest.dto.SavedNoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.example.alarm.domain.Notice}.
 */
public interface SavedNoticeService {
    /**
     * Save a notice.
     *
     * @param SavedNoticeDTO the entity to save.
     * @return the persisted entity.
     */
    SavedNoticeDTO save(Long SavedNoticeDTO);


    Page<SavedNoticeDTO> findAllByUserId(Pageable pageable, String userId);

    /**
     * Delete the "id" notice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

}
