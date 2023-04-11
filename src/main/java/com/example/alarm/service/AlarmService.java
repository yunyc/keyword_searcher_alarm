package com.example.alarm.service;

import com.example.alarm.domain.Alarm;
import com.example.alarm.web.rest.dto.AlarmDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link Alarm}.
 */
public interface AlarmService {
    /**
     * Save a alarm.
     *
     * @param alarmDTO the entity to save.
     * @return the persisted entity.
     */
    Alarm save(Alarm alarm) throws ExecutionException, InterruptedException, IOException;

    /**
     * Updates a alarm.
     *
     * @param alarmDTO the entity to update.
     * @return the persisted entity.
     */
    Alarm update(Alarm alarm) throws ExecutionException, InterruptedException, IOException;

    /**
     * Partially updates a alarm.
     *
     * @param alarmDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlarmDTO> partialUpdate(AlarmDTO alarmDTO);

    /**
     * Get all the alarms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlarmDTO> findAll(Pageable pageable);

    /**
     * Get all the alarms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlarmDTO> findAllByUserId(Pageable pageabl, String userId);

    /**
     * Get the "id" alarm.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlarmDTO> findOne(Long id);

    /**
     * Delete the "id" alarm.
     *
     * @param id the id of the entity.
     */
    void delete(Long id) throws ExecutionException, InterruptedException, JsonProcessingException;

    void sendAlarmEvent(String eventType, Alarm alarm) throws InterruptedException, ExecutionException, JsonProcessingException;
}
