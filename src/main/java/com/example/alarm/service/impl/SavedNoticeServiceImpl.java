package com.example.alarm.service.impl;

import com.example.alarm.domain.Notice;
import com.example.alarm.domain.SavedNotice;
import com.example.alarm.repository.NoticeRepository;
import com.example.alarm.repository.SavedNoticeRepository;
import com.example.alarm.service.FCMNotificationService;
import com.example.alarm.service.NoticeService;
import com.example.alarm.service.SavedNoticeService;
import com.example.alarm.web.rest.dto.NoticeDTO;
import com.example.alarm.web.rest.dto.SavedNoticeDTO;
import com.example.alarm.web.rest.errors.BadRequestAlertException;
import com.example.alarm.web.rest.mapper.NoticeMapper;
import com.example.alarm.web.rest.mapper.SavedNoticeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Notice}.
 */
@Service
@Transactional
public class SavedNoticeServiceImpl implements SavedNoticeService {

    private final Logger log = LoggerFactory.getLogger(SavedNoticeServiceImpl.class);

    private final NoticeService noticeService;

    private final SavedNoticeMapper savedNoticeMapper;

    private final SavedNoticeRepository savedNoticeRepository;


    public SavedNoticeServiceImpl(NoticeService noticeService, SavedNoticeMapper savedNoticeMapper, SavedNoticeRepository savedNoticeRepository) {
        this.noticeService = noticeService;
        this.savedNoticeRepository = savedNoticeRepository;
        this.savedNoticeMapper = savedNoticeMapper;
    }

    @Override
    public SavedNoticeDTO save(Long noticeId) {
        log.debug("Request to save Notice : {}", noticeId);
        SavedNotice sNotice = savedNoticeRepository.findByNoticeId(noticeId);

        NoticeDTO notice =  noticeService.findOne(noticeId).get();

        SavedNotice savedNotice = new SavedNotice();
        savedNotice.setContent(notice.getContent());
        savedNotice.setSiteUrl(notice.getSiteUrl());
        savedNotice.setUserId(notice.getUserId());
        savedNotice.setCrawledDate(notice.getCrawledDate());
        savedNotice.setNotiecId(notice.getId());

        if (sNotice == null) {
            savedNotice = savedNoticeRepository.save(savedNotice);
        } else {
            throw new BadRequestAlertException("이미 보관되어 있습니다.", "", "idinvalid");
        }

        return savedNoticeMapper.toDto(savedNotice);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<SavedNoticeDTO> findAllByUserId(Pageable pageable, String userId) {
        log.debug("Request to get all Alarms");

        SavedNotice notice = new SavedNotice();
        notice.setUserId(userId);

        Example<SavedNotice> userExample = Example.of(notice);
        return savedNoticeRepository.findAll(userExample, pageable).map(savedNoticeMapper::toDto);
    }


    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notice : {}", id);
        savedNoticeRepository.deleteById(id);
    }

}
