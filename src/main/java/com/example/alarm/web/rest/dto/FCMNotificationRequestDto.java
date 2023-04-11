package com.example.alarm.web.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {

    private String targetUserId;
    private String title;
    private String body;

    // private String image;
    // private Map<String, String> data;

    @Builder
    public FCMNotificationRequestDto(String targetUserId, String title, String body) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.body = body;
        // this.image = image;
        // this.data = data;
    }
}
