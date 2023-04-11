package com.example.alarm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.alarm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlarmTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alarm.class);
        Alarm alarm1 = new Alarm();
        alarm1.setId(1L);
        Alarm alarm2 = new Alarm();
        alarm2.setId(alarm1.getId());
        assertThat(alarm1).isEqualTo(alarm2);
        alarm2.setId(2L);
        assertThat(alarm1).isNotEqualTo(alarm2);
        alarm1.setId(null);
        assertThat(alarm1).isNotEqualTo(alarm2);
    }
}
