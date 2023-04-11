package com.example.alarm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.alarm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NoticeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notice.class);
        Notice notice1 = new Notice();
        notice1.setId(1L);
        Notice notice2 = new Notice();
        notice2.setId(notice1.getId());
        assertThat(notice1).isEqualTo(notice2);
        notice2.setId(2L);
        assertThat(notice1).isNotEqualTo(notice2);
        notice1.setId(null);
        assertThat(notice1).isNotEqualTo(notice2);
    }
}
