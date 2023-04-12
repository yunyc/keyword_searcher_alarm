package com.example.alarm.web.rest;

import com.example.alarm.repository.NoticeRepository;
import com.example.alarm.repository.SavedNoticeRepository;
import com.example.alarm.service.NoticeService;
import com.example.alarm.service.SavedNoticeService;
import com.example.alarm.web.rest.dto.SavedNoticeDTO;
import com.example.alarm.web.rest.errors.BadRequestAlertException;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.example.alarm.domain.Notice}.
 */
@RestController
@RequestMapping("/api")
public class SavedNoticeResource {

    private final Logger log = LoggerFactory.getLogger(SavedNoticeResource.class);

    private static final String ENTITY_NAME = "alarmNotice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SavedNoticeService savedNoticeService;

    private final SavedNoticeRepository savedNoticeRepository;

    public SavedNoticeResource(SavedNoticeService savedNoticeService, SavedNoticeRepository savedNoticeRepository) {
        this.savedNoticeService = savedNoticeService;
        this.savedNoticeRepository = savedNoticeRepository;
    }


    @GetMapping("/saved-notices")
    public ResponseEntity<List<SavedNoticeDTO>> getAllSavedNotices(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam String userId
    ) {
        log.debug("REST request to get a page of Notices");
        Page<SavedNoticeDTO> page = savedNoticeService.findAllByUserId(pageable, userId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/saved-notices/{id}")
    public ResponseEntity<SavedNoticeDTO> createSavedNotice(@PathVariable Long id) throws URISyntaxException {
        SavedNoticeDTO result = savedNoticeService.save(id);
        return ResponseEntity
            .created(new URI("/api/notices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/saved-notices/{id}")
    public ResponseEntity<Void> deleteSavedNotice(@PathVariable Long id) {
        log.debug("REST request to delete Notice : {}", id);
        savedNoticeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
