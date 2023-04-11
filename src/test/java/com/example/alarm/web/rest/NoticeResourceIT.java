package com.example.alarm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.alarm.IntegrationTest;
import com.example.alarm.domain.Notice;
import com.example.alarm.repository.NoticeRepository;
import com.example.alarm.web.rest.dto.NoticeDTO;
import com.example.alarm.web.rest.mapper.NoticeMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NoticeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NoticeResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SAVE_ENABLED = false;
    private static final Boolean UPDATED_SAVE_ENABLED = true;

    private static final Boolean DEFAULT_VISIABLED = false;
    private static final Boolean UPDATED_VISIABLED = true;

    private static final LocalDate DEFAULT_CRAWLED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CRAWLED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/notices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNoticeMockMvc;

    private Notice notice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notice createEntity(EntityManager em) {
        Notice notice = new Notice()
            .content(DEFAULT_CONTENT)
            .saveEnabled(DEFAULT_SAVE_ENABLED)
            .visiabled(DEFAULT_VISIABLED)
            .crawledDate(DEFAULT_CRAWLED_DATE);
        return notice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notice createUpdatedEntity(EntityManager em) {
        Notice notice = new Notice()
            .content(UPDATED_CONTENT)
            .saveEnabled(UPDATED_SAVE_ENABLED)
            .visiabled(UPDATED_VISIABLED)
            .crawledDate(UPDATED_CRAWLED_DATE);
        return notice;
    }

    @BeforeEach
    public void initTest() {
        notice = createEntity(em);
    }

    @Test
    @Transactional
    void createNotice() throws Exception {
        int databaseSizeBeforeCreate = noticeRepository.findAll().size();
        // Create the Notice
        NoticeDTO noticeDTO = noticeMapper.toDto(notice);
        restNoticeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeCreate + 1);
        Notice testNotice = noticeList.get(noticeList.size() - 1);
        assertThat(testNotice.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testNotice.getSaveEnabled()).isEqualTo(DEFAULT_SAVE_ENABLED);
        assertThat(testNotice.getVisiabled()).isEqualTo(DEFAULT_VISIABLED);
        assertThat(testNotice.getCrawledDate()).isEqualTo(DEFAULT_CRAWLED_DATE);
    }

    @Test
    @Transactional
    void createNoticeWithExistingId() throws Exception {
        // Create the Notice with an existing ID
        notice.setId(1L);
        NoticeDTO noticeDTO = noticeMapper.toDto(notice);

        int databaseSizeBeforeCreate = noticeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoticeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = noticeRepository.findAll().size();
        // set the field null
        notice.setContent(null);

        // Create the Notice, which fails.
        NoticeDTO noticeDTO = noticeMapper.toDto(notice);

        restNoticeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotices() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        // Get all the noticeList
        restNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notice.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].saveEnabled").value(hasItem(DEFAULT_SAVE_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].visiabled").value(hasItem(DEFAULT_VISIABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].crawledDate").value(hasItem(DEFAULT_CRAWLED_DATE.toString())));
    }

    @Test
    @Transactional
    void getNotice() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        // Get the notice
        restNoticeMockMvc
            .perform(get(ENTITY_API_URL_ID, notice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notice.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.saveEnabled").value(DEFAULT_SAVE_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.visiabled").value(DEFAULT_VISIABLED.booleanValue()))
            .andExpect(jsonPath("$.crawledDate").value(DEFAULT_CRAWLED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNotice() throws Exception {
        // Get the notice
        restNoticeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotice() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();

        // Update the notice
        Notice updatedNotice = noticeRepository.findById(notice.getId()).get();
        // Disconnect from session so that the updates on updatedNotice are not directly saved in db
        em.detach(updatedNotice);
        updatedNotice
            .content(UPDATED_CONTENT)
            .saveEnabled(UPDATED_SAVE_ENABLED)
            .visiabled(UPDATED_VISIABLED)
            .crawledDate(UPDATED_CRAWLED_DATE);
        NoticeDTO noticeDTO = noticeMapper.toDto(updatedNotice);

        restNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, noticeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
        Notice testNotice = noticeList.get(noticeList.size() - 1);
        assertThat(testNotice.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotice.getSaveEnabled()).isEqualTo(UPDATED_SAVE_ENABLED);
        assertThat(testNotice.getVisiabled()).isEqualTo(UPDATED_VISIABLED);
        assertThat(testNotice.getCrawledDate()).isEqualTo(UPDATED_CRAWLED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingNotice() throws Exception {
        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();
        notice.setId(count.incrementAndGet());

        // Create the Notice
        NoticeDTO noticeDTO = noticeMapper.toDto(notice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, noticeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotice() throws Exception {
        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();
        notice.setId(count.incrementAndGet());

        // Create the Notice
        NoticeDTO noticeDTO = noticeMapper.toDto(notice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotice() throws Exception {
        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();
        notice.setId(count.incrementAndGet());

        // Create the Notice
        NoticeDTO noticeDTO = noticeMapper.toDto(notice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoticeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNoticeWithPatch() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();

        // Update the notice using partial update
        Notice partialUpdatedNotice = new Notice();
        partialUpdatedNotice.setId(notice.getId());

        partialUpdatedNotice.content(UPDATED_CONTENT).visiabled(UPDATED_VISIABLED).crawledDate(UPDATED_CRAWLED_DATE);

        restNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotice.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotice))
            )
            .andExpect(status().isOk());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
        Notice testNotice = noticeList.get(noticeList.size() - 1);
        assertThat(testNotice.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotice.getSaveEnabled()).isEqualTo(DEFAULT_SAVE_ENABLED);
        assertThat(testNotice.getVisiabled()).isEqualTo(UPDATED_VISIABLED);
        assertThat(testNotice.getCrawledDate()).isEqualTo(UPDATED_CRAWLED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateNoticeWithPatch() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();

        // Update the notice using partial update
        Notice partialUpdatedNotice = new Notice();
        partialUpdatedNotice.setId(notice.getId());

        partialUpdatedNotice
            .content(UPDATED_CONTENT)
            .saveEnabled(UPDATED_SAVE_ENABLED)
            .visiabled(UPDATED_VISIABLED)
            .crawledDate(UPDATED_CRAWLED_DATE);

        restNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotice.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotice))
            )
            .andExpect(status().isOk());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
        Notice testNotice = noticeList.get(noticeList.size() - 1);
        assertThat(testNotice.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotice.getSaveEnabled()).isEqualTo(UPDATED_SAVE_ENABLED);
        assertThat(testNotice.getVisiabled()).isEqualTo(UPDATED_VISIABLED);
        assertThat(testNotice.getCrawledDate()).isEqualTo(UPDATED_CRAWLED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingNotice() throws Exception {
        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();
        notice.setId(count.incrementAndGet());

        // Create the Notice
        NoticeDTO noticeDTO = noticeMapper.toDto(notice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, noticeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotice() throws Exception {
        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();
        notice.setId(count.incrementAndGet());

        // Create the Notice
        NoticeDTO noticeDTO = noticeMapper.toDto(notice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotice() throws Exception {
        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();
        notice.setId(count.incrementAndGet());

        // Create the Notice
        NoticeDTO noticeDTO = noticeMapper.toDto(notice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(noticeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notice in the database
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotice() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        int databaseSizeBeforeDelete = noticeRepository.findAll().size();

        // Delete the notice
        restNoticeMockMvc
            .perform(delete(ENTITY_API_URL_ID, notice.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
