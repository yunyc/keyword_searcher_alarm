package com.example.alarm.adapter;

import com.example.alarm.domain.event.AlarmChanged;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.ExecutionException;

public interface AlarmProducer {
    void sendAlarmCreatedEvent(AlarmChanged alarmChanged) throws ExecutionException, InterruptedException, JsonProcessingException;

    void sendAlarmDeletedEvent(AlarmChanged alarmChanged) throws ExecutionException, InterruptedException, JsonProcessingException;
}
