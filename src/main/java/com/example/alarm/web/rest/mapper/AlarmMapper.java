package com.example.alarm.web.rest.mapper;

import com.example.alarm.domain.Alarm;
import com.example.alarm.web.rest.dto.AlarmDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alarm} and its DTO {@link AlarmDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlarmMapper extends EntityMapper<AlarmDTO, Alarm> {
    /*

    @Override
    default Alarm toEntity(AlarmDTO dto) {
        Alarm alarm = new Alarm();
        alarm.setDescription(dto.getDescription());
        alarm.setId(dto.getId());
        alarm.setUserId(dto.getUserId());
        alarm.setCrawlingDate(dto.getCrawlingDate());
        alarm.setCreatedDate(dto.getCreatedDate());
        alarm.setRefeshTime(dto.getRefeshTime());
        alarm.setMusicPath(dto.getMusicPath());
        alarm.setMusicTitle(dto.getMusicTitle());
        alarm.setSiteUrl(dto.getSiteUrl());
        alarm.setUseSwitch(dto.getUseSwitch());
        alarm.setVbEnabled(dto.getVbEnabled());
        dto.getKeywords().forEach((item) -> alarm.addKeyword(KeywordMapper.INSTANCE.toEntity(item)));
        return alarm;
    }

    @Override
    default AlarmDTO toDto(Alarm entity) {
        AlarmDTO dto = new AlarmDTO();
        dto.setDescription(entity.getDescription());
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setCrawlingDate(entity.getCrawlingDate());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setRefeshTime(entity.getRefeshTime());
        dto.setMusicPath(entity.getMusicPath());
        dto.setMusicTitle(entity.getMusicTitle());
        dto.setSiteUrl(entity.getSiteUrl());
        dto.setUseSwitch(entity.getUseSwitch());
        dto.setVbEnabled(entity.getVbEnabled());
        entity.getKeywords().forEach((item) -> dto.
            addKeyword(KeywordMapper.INSTANCE.toDto(item)));
        return dto;
    }
    */

}
