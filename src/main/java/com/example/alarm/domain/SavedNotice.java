package com.example.alarm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "saved_notice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SavedNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "site_url", nullable = false)
    private String siteUrl;

    @Column(name = "crawled_date")
    private LocalDate crawledDate;

    @Column(name = "notice_id")
    private Long noticeId;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SavedNotice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public SavedNotice userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public SavedNotice content(String content) {
        this.setContent(content);
        return this;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getSiteUrl() {
        return this.siteUrl;
    }

    public SavedNotice siteUrl(String siteUrl) {
        this.setSiteUrl(siteUrl);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCrawledDate() {
        return this.crawledDate;
    }

    public SavedNotice crawledDate(LocalDate crawledDate) {
        this.setCrawledDate(crawledDate);
        return this;
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

    public SavedNotice noticeId(Long noticeId) {
        this.setNoticeId(noticeId);
        return this;
    }


// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SavedNotice)) {
            return false;
        }
        return id != null && id.equals(((SavedNotice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SavedNotice{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", crawledDate='" + getCrawledDate() + "'" +
            "}";
    }
}
