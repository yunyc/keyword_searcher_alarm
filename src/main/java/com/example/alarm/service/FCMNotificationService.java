package com.example.alarm.service;

import com.example.alarm.domain.Alarm;
import com.example.alarm.web.rest.dto.AlarmDTO;
import com.example.alarm.web.rest.dto.FCMNotificationRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FCMNotificationService {
    String sendNotificationByToken(FCMNotificationRequestDto requestDto);
}
