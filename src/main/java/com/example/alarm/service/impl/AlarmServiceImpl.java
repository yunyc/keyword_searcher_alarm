package com.example.alarm.service.impl;

import com.example.alarm.adapter.AlarmProducer;
import com.example.alarm.domain.Alarm;
import com.example.alarm.domain.event.AlarmChanged;
import com.example.alarm.repository.AlarmRepository;
import com.example.alarm.service.AlarmService;
import com.example.alarm.service.FCMNotificationService;
import com.example.alarm.web.rest.dto.AlarmDTO;
import com.example.alarm.web.rest.dto.FCMNotificationRequestDto;
import com.example.alarm.web.rest.mapper.AlarmMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Alarm}.
 */
@Service
@Transactional
public class AlarmServiceImpl implements AlarmService {

    private final Logger log = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final AlarmRepository alarmRepository;

    private final AlarmMapper alarmMapper;

    private final AlarmProducer alarmProducer;


    public AlarmServiceImpl(
        AlarmRepository alarmRepository,
        AlarmMapper alarmMapper,
        AlarmProducer alarmProducer
    ) {
        this.alarmRepository = alarmRepository;
        this.alarmMapper = alarmMapper;
        this.alarmProducer = alarmProducer;
    }

    @Override
    public Alarm save(Alarm alarm) throws ExecutionException, InterruptedException, IOException {
        log.debug("Request to save Alarm : {}", alarm);

        alarm.preCrawlAndSave();

        Alarm savedAlarm = alarmRepository.save(alarm);
        this.sendAlarmEvent("NEW_ALARM", savedAlarm);

        return savedAlarm;
    }

    @Override
    public Alarm update(Alarm alarm) throws ExecutionException, InterruptedException, IOException {
        log.debug("Request to update Alarm : {}", alarm);

        alarm.preCrawlAndSave();

        Alarm savedAlarm = alarmRepository.save(alarm);
        this.sendAlarmEvent("UPDATE_ALARM", savedAlarm);

        return savedAlarm;
    }

    @Override
    public Optional<AlarmDTO> partialUpdate(AlarmDTO alarmDTO) {
        log.debug("Request to partially update Alarm : {}", alarmDTO);

        return alarmRepository
            .findById(alarmDTO.getId())
            .map(existingAlarm -> {
                alarmMapper.partialUpdate(existingAlarm, alarmDTO);

                return existingAlarm;
            })
            .map(alarmRepository::save)
            .map(alarmMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlarmDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Alarms");
        return alarmRepository.findAll(pageable).map(alarmMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlarmDTO> findAllByUserId(Pageable pageable, String userId) {
        log.debug("Request to get all Alarms");


        Alarm alarm = new Alarm();
        alarm.setUserId(userId);

        Example<Alarm> userExample = Example.of(alarm);
        return alarmRepository.findAll(userExample, pageable).map(alarmMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlarmDTO> findOne(Long id) {
        log.debug("Request to get Alarm : {}", id);
        return alarmRepository.findById(id).map(alarmMapper::toDto);
    }

    @Override
    public void delete(Long id) throws ExecutionException, InterruptedException, JsonProcessingException {
        log.debug("Request to delete Alarm : {}", id);

        Alarm alarm = new Alarm();
        alarm.setId(id);

        alarmRepository.deleteById(id);
        this.sendAlarmEvent("DELETE_ALARM", alarm);
    }

    @Override
    public void sendAlarmEvent(String eventType, Alarm alarm) throws InterruptedException, ExecutionException, JsonProcessingException {
        AlarmChanged alarmChanged = new AlarmChanged();
        if (eventType.equals("NEW_ALARM") || eventType.equals("UPDATE_ALARM")) {
            alarmChanged.setAlarmId(alarm.getId());
            alarmChanged.setKeyword(alarm.getKeyword());
            alarmChanged.setDescription(alarm.getDescription());
            alarmChanged.setSiteUrl(alarm.getSiteUrl());
            alarmChanged.setRefeshTime(alarm.getRefeshTime());
            alarmChanged.setVbEnabled(alarm.getVbEnabled());
            alarm.getNotices().forEach((notice -> alarmChanged.getExcludeUrl().add(notice.getSiteUrl())));
            alarmChanged.setEventType(eventType);
            alarmProducer.sendAlarmCreatedEvent(alarmChanged);
        } else if (eventType.equals("DELETE_ALARM")) {
            alarmChanged.setEventType(eventType);
            alarmChanged.setAlarmId(alarm.getId());
            alarmProducer.sendAlarmDeletedEvent(alarmChanged);
        }
    }
}
