package com.example.alarm.web.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.example.alarm.domain.Notice} entity.
 */
@Schema(description = "not an ignored comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NoticeDTO implements Serializable {

    private Long id;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NotNull
    private String content;

    private String siteUrl;

    private Boolean saveEnabled;

    private Boolean visiabled;

    private LocalDate crawledDate;

    private AlarmDTO alarm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Boolean getSaveEnabled() {
        return saveEnabled;
    }

    public void setSaveEnabled(Boolean saveEnabled) {
        this.saveEnabled = saveEnabled;
    }

    public Boolean getVisiabled() {
        return visiabled;
    }

    public void setVisiabled(Boolean visiabled) {
        this.visiabled = visiabled;
    }

    public LocalDate getCrawledDate() {
        return crawledDate;
    }

    public void setCrawledDate(LocalDate crawledDate) {
        this.crawledDate = crawledDate;
    }

    public AlarmDTO getAlarm() {
        return alarm;
    }

    public void setAlarm(AlarmDTO alarm) {
        this.alarm = alarm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoticeDTO)) {
            return false;
        }

        NoticeDTO noticeDTO = (NoticeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, noticeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoticeDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", saveEnabled='" + getSaveEnabled() + "'" +
            ", visiabled='" + getVisiabled() + "'" +
            ", crawledDate='" + getCrawledDate() + "'" +
            ", alarm=" + getAlarm() +
            "}";
    }
}
