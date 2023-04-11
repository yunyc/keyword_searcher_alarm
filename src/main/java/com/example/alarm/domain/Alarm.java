package com.example.alarm.domain;

import com.example.alarm.domain.enumeration.SelectedTime;
import com.example.alarm.domain.event.NoticeChanged;
import com.example.alarm.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.IOException;
import java.io.Serializable;
import java.security.Key;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A Alarm.
 */
@Entity
@Table(name = "alarm")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "keyword")
    private String keyword;

    @NotNull
    //@Pattern(regexp = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$")
    @Column(name = "site_url", nullable = false)
    private String siteUrl;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "refesh_time", nullable = false)
    private SelectedTime refeshTime;

    @Column(name = "vb_enabled")
    private Boolean vbEnabled;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "crawling_date")
    private LocalDate crawlingDate;

    @OneToMany(mappedBy = "alarm", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alarm" }, allowSetters = true)
    private Set<Notice> notices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alarm id(Long id) {
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

    public Alarm userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Alarm keyword(String keyword) {
        this.setKeyword(keyword);
        return this;
    }

    public String getSiteUrl() {
        return this.siteUrl;
    }

    public Alarm siteUrl(String siteUrl) {
        this.setSiteUrl(siteUrl);
        return this;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public Alarm description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectedTime getRefeshTime() {
        return this.refeshTime;
    }

    public Alarm refeshTime(SelectedTime refeshTime) {
        this.setRefeshTime(refeshTime);
        return this;
    }

    public void setRefeshTime(SelectedTime refeshTime) {
        this.refeshTime = refeshTime;
    }

    public Boolean getVbEnabled() {
        return this.vbEnabled;
    }

    public Alarm vbEnabled(Boolean vbEnabled) {
        this.setVbEnabled(vbEnabled);
        return this;
    }

    public void setVbEnabled(Boolean vbEnabled) {
        this.vbEnabled = vbEnabled;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Alarm createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getCrawlingDate() {
        return this.crawlingDate;
    }

    public Alarm crawlingDate(LocalDate crawlingDate) {
        this.setCrawlingDate(crawlingDate);
        return this;
    }

    public void setCrawlingDate(LocalDate crawlingDate) {
        this.crawlingDate = crawlingDate;
    }

    public Set<Notice> getNotices() {
        return this.notices;
    }

    public void setNotices(Set<Notice> notices) {
        if (this.notices != null) {
            this.notices.forEach(i -> i.setAlarm(null));
        }
        if (notices != null) {
            notices.forEach(i -> i.setAlarm(this));
        }
        this.notices = notices;
    }

    public Alarm notices(Set<Notice> notices) {
        this.setNotices(notices);
        return this;
    }

    public Alarm addNotice(Notice notice) {
        this.notices.add(notice);
        notice.setAlarm(this);
        return this;
    }

    public Alarm removeNotice(Notice notice) {
        this.notices.remove(notice);
        notice.setAlarm(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alarm)) {
            return false;
        }
        return id != null && id.equals(((Alarm) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alarm{" +
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

    public void preCrawlAndSave() throws IOException {
        // 웹 크롤링 코드
        System.out.println(this.siteUrl);
        Document doc = Jsoup.connect(this.siteUrl).get(); // 웹사이트 HTML 가져오기

        for (String keyword : keyword.split(",")) {
            Elements elements = doc.select("a:contains(" + keyword + ")"); // a 태그에서 "키워드"가 포함된 태그 선택

            for (Element element : elements) {
                Notice notice = new Notice();
                notice.setAlarm(this);
                notice.setSiteUrl(this.siteUrl);
                notice.setContent(element.text());
                notice.setVisiabled(false);

                this.notices.add(notice);
            }
        }
    }
}
