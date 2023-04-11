package com.example.alarm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Entity
@Table(name = "notice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notice implements Serializable {

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

    @Column(name = "save_enabled")
    private Boolean saveEnabled;

    @Column(name = "visiabled")
    private Boolean visiabled;

    @Column(name = "crawled_date")
    private LocalDate crawledDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "keywords", "notices" }, allowSetters = true)
    private Alarm alarm;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notice id(Long id) {
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

    public Notice userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public Notice content(String content) {
        this.setContent(content);
        return this;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getSiteUrl() {
        return this.siteUrl;
    }

    public Notice siteUrl(String siteUrl) {
        this.setSiteUrl(siteUrl);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSaveEnabled() {
        return this.saveEnabled;
    }

    public Notice saveEnabled(Boolean saveEnabled) {
        this.setSaveEnabled(saveEnabled);
        return this;
    }

    public void setSaveEnabled(Boolean saveEnabled) {
        this.saveEnabled = saveEnabled;
    }

    public Boolean getVisiabled() {
        return this.visiabled;
    }

    public Notice visiabled(Boolean visiabled) {
        this.setVisiabled(visiabled);
        return this;
    }

    public void setVisiabled(Boolean visiabled) {
        this.visiabled = visiabled;
    }

    public LocalDate getCrawledDate() {
        return this.crawledDate;
    }

    public Notice crawledDate(LocalDate crawledDate) {
        this.setCrawledDate(crawledDate);
        return this;
    }

    public void setCrawledDate(LocalDate crawledDate) {
        this.crawledDate = crawledDate;
    }

    public Alarm getAlarm() {
        return this.alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public Notice alarm(Alarm alarm) {
        this.setAlarm(alarm);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notice)) {
            return false;
        }
        return id != null && id.equals(((Notice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notice{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", saveEnabled='" + getSaveEnabled() + "'" +
            ", visiabled='" + getVisiabled() + "'" +
            ", crawledDate='" + getCrawledDate() + "'" +
            "}";
    }
}
