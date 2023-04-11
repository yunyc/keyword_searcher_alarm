package com.example.alarm.web.rest.mapper;

import com.example.alarm.domain.Alarm;
import com.example.alarm.domain.Notice;
import com.example.alarm.domain.User;
import com.example.alarm.web.rest.dto.AlarmDTO;
import com.example.alarm.web.rest.dto.NoticeDTO;
import com.example.alarm.web.rest.dto.UserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Notice} and its DTO {@link NoticeDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {}
