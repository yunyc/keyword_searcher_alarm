package com.example.alarm.web.rest;

import com.example.alarm.repository.AlarmRepository;
import com.example.alarm.service.AlarmService;
import com.example.alarm.web.rest.dto.AlarmDTO;
import com.example.alarm.web.rest.errors.BadRequestAlertException;
import com.example.alarm.web.rest.mapper.AlarmMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.example.alarm.domain.Alarm}.
 */
@RestController
@RequestMapping("/api")
public class AlarmResource {

    private final Logger log = LoggerFactory.getLogger(AlarmResource.class);

    private static final String ENTITY_NAME = "alarmAlarm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlarmService alarmService;

    private final AlarmRepository alarmRepository;

    private final AlarmMapper alarmMapper;

    public AlarmResource(AlarmService alarmService, AlarmRepository alarmRepository, AlarmMapper alarmMapper) {
        this.alarmService = alarmService;
        this.alarmRepository = alarmRepository;
        this.alarmMapper = alarmMapper;
    }

    /**
     * {@code POST  /alarms} : 알람 생성
     *
     * @param alarmDTO the alarmDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alarmDTO, or with status {@code 400 (Bad Request)} if the alarm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alarms")
    public ResponseEntity<AlarmDTO> createAlarm(@Valid @RequestBody AlarmDTO alarmDTO)
        throws URISyntaxException, ExecutionException, InterruptedException, IOException {
        log.debug("REST request to save Alarm : {}", alarmDTO);

        AlarmDTO result = alarmMapper.toDto(alarmService.save(alarmMapper.toEntity(alarmDTO)));
        return ResponseEntity
            .created(new URI("/api/alarms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alarms/:id} : 알람 변경
     *
     * @param id the id of the alarmDTO to save.
     * @param alarmDTO the alarmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alarmDTO,
     * or with status {@code 400 (Bad Request)} if the alarmDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alarmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alarms/{id}")
    public ResponseEntity<AlarmDTO> updateAlarm(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlarmDTO alarmDTO
    ) throws URISyntaxException, ExecutionException, InterruptedException, IOException {
        log.debug("REST request to update Alarm : {}, {}", id, alarmDTO);
        if (alarmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alarmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alarmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AlarmDTO result = alarmMapper.toDto(alarmService.update(alarmMapper.toEntity(alarmDTO)));
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alarmDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /alarms/:id} : Partial updates given fields of an existing alarm, field will ignore if it is null
     *
     * @param id the id of the alarmDTO to save.
     * @param alarmDTO the alarmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alarmDTO,
     * or with status {@code 400 (Bad Request)} if the alarmDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alarmDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alarmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/alarms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlarmDTO> partialUpdateAlarm(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlarmDTO alarmDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Alarm partially : {}, {}", id, alarmDTO);
        if (alarmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alarmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alarmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlarmDTO> result = alarmService.partialUpdate(alarmDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alarmDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /alarms} : 알람 목록 조회
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alarms in body.

    @GetMapping("/alarms")
    public ResponseEntity<List<AlarmDTO>> getAllAlarms(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Alarms");
        Page<AlarmDTO> page = alarmService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
     */

    /**
     * {@code GET  /alarms} : 알람 목록 조회
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alarms in body.
     */
    @GetMapping("/alarms")
    public ResponseEntity<List<AlarmDTO>> getAllAlarmsByUserId(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam String userId
    ) {
        log.debug("REST request to get a page of Alarms");
        Page<AlarmDTO> page = alarmService.findAllByUserId(pageable, userId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alarms/:id} : 알람 상세 조회
     *
     * @param id the id of the alarmDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alarmDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alarms/{id}")
    public ResponseEntity<AlarmDTO> getAlarm(@PathVariable Long id) {
        log.debug("REST request to get Alarm : {}", id);
        Optional<AlarmDTO> alarmDTO = alarmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alarmDTO);
    }

    /**
     * {@code DELETE  /alarms/:id} : 알람 삭제
     *
     * @param id the id of the alarmDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alarms/{id}")
    public ResponseEntity<Void> deleteAlarm(@PathVariable Long id)
        throws ExecutionException, InterruptedException, JsonProcessingException {
        log.debug("REST request to delete Alarm : {}", id);
        alarmService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
