package com.example.alarm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.alarm.web.rest.mapper.NoticeMapper;
import com.example.alarm.web.rest.mapper.NoticeMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NoticeMapperTest {

    private NoticeMapper noticeMapper;

    @BeforeEach
    public void setUp() {
        noticeMapper = new NoticeMapperImpl();
    }
}
