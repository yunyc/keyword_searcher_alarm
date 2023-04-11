package com.example.alarm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.alarm.web.rest.mapper.AlarmMapper;
import com.example.alarm.web.rest.mapper.AlarmMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlarmMapperTest {

    private AlarmMapper alarmMapper;

    @BeforeEach
    public void setUp() {
        alarmMapper = new AlarmMapperImpl();
    }
}
