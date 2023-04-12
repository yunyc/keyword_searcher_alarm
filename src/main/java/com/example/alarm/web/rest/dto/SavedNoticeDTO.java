package com.example.alarm.web.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.alarm.domain.Notice} entity.
 */
@Schema(description = "not an ignored comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SavedNoticeDTO implements Serializable {

    private Long id;

    private String userId;

    @NotNull
    private String content;

    private String siteUrl;

    private LocalDate crawledDate;

    private Long noticeId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public LocalDate getCrawledDate() {
        return crawledDate;
    }

    public void setCrawledDate(LocalDate crawledDate) {
        this.crawledDate = crawledDate;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SavedNoticeDTO)) {
            return false;
        }

        SavedNoticeDTO noticeDTO = (SavedNoticeDTO) o;
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
            ", crawledDate='" + getCrawledDate() + "'" +
            "}";
    }
}
