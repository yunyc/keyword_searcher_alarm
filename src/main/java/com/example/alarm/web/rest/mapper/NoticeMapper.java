package com.example.alarm.web.rest.mapper;

import com.example.alarm.domain.Alarm;
import com.example.alarm.domain.Notice;
import com.example.alarm.web.rest.dto.AlarmDTO;
import com.example.alarm.web.rest.dto.NoticeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notice} and its DTO {@link NoticeDTO}.
 */
@Mapper(componentModel = "spring")
public interface NoticeMapper extends EntityMapper<NoticeDTO, Notice> {
    @Mapping(target = "alarm", source = "alarm", qualifiedByName = "alarmId")
    NoticeDTO toDto(Notice s);

    @Named("alarmId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlarmDTO toDtoAlarmId(Alarm alarm);
}
