package com.example.alarm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.alarm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlarmDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlarmDTO.class);
        AlarmDTO alarmDTO1 = new AlarmDTO();
        alarmDTO1.setId(1L);
        AlarmDTO alarmDTO2 = new AlarmDTO();
        assertThat(alarmDTO1).isNotEqualTo(alarmDTO2);
        alarmDTO2.setId(alarmDTO1.getId());
        assertThat(alarmDTO1).isEqualTo(alarmDTO2);
        alarmDTO2.setId(2L);
        assertThat(alarmDTO1).isNotEqualTo(alarmDTO2);
        alarmDTO1.setId(null);
        assertThat(alarmDTO1).isNotEqualTo(alarmDTO2);
    }
}
