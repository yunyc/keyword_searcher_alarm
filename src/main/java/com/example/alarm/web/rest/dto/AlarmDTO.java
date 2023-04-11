package com.example.alarm.web.rest.dto;

import com.example.alarm.domain.enumeration.SelectedTime;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.example.alarm.domain.Alarm} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlarmDTO implements Serializable {

    private Long id;

    private String userId;

    private String keyword;

    @NotNull
    //@Pattern(regexp = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$")
    private String siteUrl;

    private String description;

    @NotNull
    private SelectedTime refeshTime;

    private Boolean vbEnabled;

    private LocalDate createdDate;

    private LocalDate crawlingDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectedTime getRefeshTime() {
        return refeshTime;
    }

    public void setRefeshTime(SelectedTime refeshTime) {
        this.refeshTime = refeshTime;
    }

    public Boolean getVbEnabled() {
        return vbEnabled;
    }

    public void setVbEnabled(Boolean vbEnabled) {
        this.vbEnabled = vbEnabled;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getCrawlingDate() {
        return crawlingDate;
    }

    public void setCrawlingDate(LocalDate crawlingDate) {
        this.crawlingDate = crawlingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlarmDTO)) {
            return false;
        }

        AlarmDTO alarmDTO = (AlarmDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alarmDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlarmDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", siteUrl='" + getSiteUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", refeshTime='" + getRefeshTime() + "'" +
            ", vbEnabled='" + getVbEnabled() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", crawlingDate='" + getCrawlingDate() + "'" +
            "}";
    }
}
