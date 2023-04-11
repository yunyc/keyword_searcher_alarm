package com.example.alarm.web.rest;

import com.example.alarm.service.FCMNotificationService;
import com.example.alarm.web.rest.dto.FCMNotificationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notification", description = "FCM Notification 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notification")
public class FCMNotificationResource {

    private final FCMNotificationService fcmNotificationService;

    @Operation(summary = "알림 보내기")
    @PostMapping
    public String sendNotificationByToken(@RequestBody FCMNotificationRequestDto requestDto) {
        return fcmNotificationService.sendNotificationByToken(requestDto);
    }
}
