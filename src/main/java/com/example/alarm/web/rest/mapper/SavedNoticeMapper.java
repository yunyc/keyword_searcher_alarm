package com.example.alarm.web.rest.mapper;

import com.example.alarm.domain.Alarm;
import com.example.alarm.domain.Notice;
import com.example.alarm.domain.SavedNotice;
import com.example.alarm.web.rest.dto.AlarmDTO;
import com.example.alarm.web.rest.dto.NoticeDTO;
import com.example.alarm.web.rest.dto.SavedNoticeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Notice} and its DTO {@link NoticeDTO}.
 */
@Mapper(componentModel = "spring")
public interface SavedNoticeMapper extends EntityMapper<SavedNoticeDTO, SavedNotice> {

}
