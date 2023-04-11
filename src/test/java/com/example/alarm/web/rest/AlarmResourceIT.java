package com.example.alarm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.alarm.IntegrationTest;
import com.example.alarm.domain.Alarm;
import com.example.alarm.domain.enumeration.SelectedTime;
import com.example.alarm.repository.AlarmRepository;
import com.example.alarm.web.rest.dto.AlarmDTO;
import com.example.alarm.web.rest.mapper.AlarmMapper;
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
 * Integration tests for the {@link AlarmResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlarmResourceIT {

    private static final String DEFAULT_USER_ID = "test";
    private static final String UPDATED_USER_ID = "testu";

    private static final String DEFAULT_SITE_URL = "Hsssbb8";
    private static final String UPDATED_SITE_URL = "Oiej6";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final SelectedTime DEFAULT_REFESH_TIME = SelectedTime.ONE;
    private static final SelectedTime UPDATED_REFESH_TIME = SelectedTime.FIVE;

    private static final String DEFAULT_MUSIC_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MUSIC_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MUSIC_PATH = "AAAAAAAAAA";
    private static final String UPDATED_MUSIC_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VB_ENABLED = false;
    private static final Boolean UPDATED_VB_ENABLED = true;

    private static final Boolean DEFAULT_USE_SWITCH = false;
    private static final Boolean UPDATED_USE_SWITCH = true;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CRAWLING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CRAWLING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/alarms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlarmMockMvc;

    private Alarm alarm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alarm createEntity(EntityManager em) {
        Alarm alarm = new Alarm()
            .userId(DEFAULT_USER_ID)
            .siteUrl(DEFAULT_SITE_URL)
            .description(DEFAULT_DESCRIPTION)
            .refeshTime(DEFAULT_REFESH_TIME)
            .vbEnabled(DEFAULT_VB_ENABLED)
            .createdDate(DEFAULT_CREATED_DATE)
            .crawlingDate(DEFAULT_CRAWLING_DATE);
        return alarm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alarm createUpdatedEntity(EntityManager em) {
        Alarm alarm = new Alarm()
            .userId(UPDATED_USER_ID)
            .siteUrl(UPDATED_SITE_URL)
            .description(UPDATED_DESCRIPTION)
            .refeshTime(UPDATED_REFESH_TIME)
            .musicPath(UPDATED_MUSIC_PATH)
            .vbEnabled(UPDATED_VB_ENABLED)
            .useSwitch(UPDATED_USE_SWITCH)
            .createdDate(UPDATED_CREATED_DATE)
            .crawlingDate(UPDATED_CRAWLING_DATE);
        return alarm;
    }

    @BeforeEach
    public void initTest() {
        alarm = createEntity(em);
    }

    @Test
    @Transactional
    void createAlarm() throws Exception {
        int databaseSizeBeforeCreate = alarmRepository.findAll().size();
        // Create the Alarm
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);
        restAlarmMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeCreate + 1);
        Alarm testAlarm = alarmList.get(alarmList.size() - 1);
        assertThat(testAlarm.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAlarm.getSiteUrl()).isEqualTo(DEFAULT_SITE_URL);
        assertThat(testAlarm.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAlarm.getRefeshTime()).isEqualTo(DEFAULT_REFESH_TIME);
        assertThat(testAlarm.getVbEnabled()).isEqualTo(DEFAULT_VB_ENABLED);
        assertThat(testAlarm.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAlarm.getCrawlingDate()).isEqualTo(DEFAULT_CRAWLING_DATE);
    }

    @Test
    @Transactional
    void createAlarmWithExistingId() throws Exception {
        // Create the Alarm with an existing ID
        alarm.setId(1L);
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        int databaseSizeBeforeCreate = alarmRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlarmMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSiteUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = alarmRepository.findAll().size();
        // set the field null
        alarm.setSiteUrl(null);

        // Create the Alarm, which fails.
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        restAlarmMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isBadRequest());

        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRefeshTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = alarmRepository.findAll().size();
        // set the field null
        alarm.setRefeshTime(null);

        // Create the Alarm, which fails.
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        restAlarmMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isBadRequest());

        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlarms() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);

        // Get all the alarmList
        restAlarmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarm.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].siteUrl").value(hasItem(DEFAULT_SITE_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].refeshTime").value(hasItem(DEFAULT_REFESH_TIME.toString())))
            .andExpect(jsonPath("$.[*].musicTitle").value(hasItem(DEFAULT_MUSIC_TITLE)))
            .andExpect(jsonPath("$.[*].musicPath").value(hasItem(DEFAULT_MUSIC_PATH)))
            .andExpect(jsonPath("$.[*].vbEnabled").value(hasItem(DEFAULT_VB_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].useSwitch").value(hasItem(DEFAULT_USE_SWITCH.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].crawlingDate").value(hasItem(DEFAULT_CRAWLING_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlarm() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);

        // Get the alarm
        restAlarmMockMvc
            .perform(get(ENTITY_API_URL_ID, alarm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alarm.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.siteUrl").value(DEFAULT_SITE_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.refeshTime").value(DEFAULT_REFESH_TIME.toString()))
            .andExpect(jsonPath("$.musicTitle").value(DEFAULT_MUSIC_TITLE))
            .andExpect(jsonPath("$.musicPath").value(DEFAULT_MUSIC_PATH))
            .andExpect(jsonPath("$.vbEnabled").value(DEFAULT_VB_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.useSwitch").value(DEFAULT_USE_SWITCH.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.crawlingDate").value(DEFAULT_CRAWLING_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAlarm() throws Exception {
        // Get the alarm
        restAlarmMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlarm() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);

        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();

        // Update the alarm
        Alarm updatedAlarm = alarmRepository.findById(alarm.getId()).get();
        // Disconnect from session so that the updates on updatedAlarm are not directly saved in db
        em.detach(updatedAlarm);
        updatedAlarm
            .userId(UPDATED_USER_ID)
            .siteUrl(UPDATED_SITE_URL)
            .description(UPDATED_DESCRIPTION)
            .refeshTime(UPDATED_REFESH_TIME)
            .musicTitle(UPDATED_MUSIC_TITLE)
            .musicPath(UPDATED_MUSIC_PATH)
            .vbEnabled(UPDATED_VB_ENABLED)
            .useSwitch(UPDATED_USE_SWITCH)
            .createdDate(UPDATED_CREATED_DATE)
            .crawlingDate(UPDATED_CRAWLING_DATE);
        AlarmDTO alarmDTO = alarmMapper.toDto(updatedAlarm);

        restAlarmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alarmDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isOk());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
        Alarm testAlarm = alarmList.get(alarmList.size() - 1);
        assertThat(testAlarm.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAlarm.getSiteUrl()).isEqualTo(UPDATED_SITE_URL);
        assertThat(testAlarm.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlarm.getRefeshTime()).isEqualTo(UPDATED_REFESH_TIME);
        assertThat(testAlarm.getVbEnabled()).isEqualTo(UPDATED_VB_ENABLED);
        assertThat(testAlarm.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAlarm.getCrawlingDate()).isEqualTo(UPDATED_CRAWLING_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAlarm() throws Exception {
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();
        alarm.setId(count.incrementAndGet());

        // Create the Alarm
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlarmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alarmDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlarm() throws Exception {
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();
        alarm.setId(count.incrementAndGet());

        // Create the Alarm
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlarmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlarm() throws Exception {
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();
        alarm.setId(count.incrementAndGet());

        // Create the Alarm
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlarmMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlarmWithPatch() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);

        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();

        // Update the alarm using partial update
        Alarm partialUpdatedAlarm = new Alarm();
        partialUpdatedAlarm.setId(alarm.getId());

        partialUpdatedAlarm.siteUrl(UPDATED_SITE_URL).description(UPDATED_DESCRIPTION).createdDate(UPDATED_CREATED_DATE);

        restAlarmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlarm.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlarm))
            )
            .andExpect(status().isOk());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
        Alarm testAlarm = alarmList.get(alarmList.size() - 1);
        assertThat(testAlarm.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAlarm.getSiteUrl()).isEqualTo(UPDATED_SITE_URL);
        assertThat(testAlarm.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlarm.getRefeshTime()).isEqualTo(DEFAULT_REFESH_TIME);
        assertThat(testAlarm.getVbEnabled()).isEqualTo(DEFAULT_VB_ENABLED);
        assertThat(testAlarm.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAlarm.getCrawlingDate()).isEqualTo(DEFAULT_CRAWLING_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAlarmWithPatch() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);

        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();

        // Update the alarm using partial update
        Alarm partialUpdatedAlarm = new Alarm();
        partialUpdatedAlarm.setId(alarm.getId());

        partialUpdatedAlarm
            .userId(UPDATED_USER_ID)
            .siteUrl(UPDATED_SITE_URL)
            .description(UPDATED_DESCRIPTION)
            .refeshTime(UPDATED_REFESH_TIME)
            .musicTitle(UPDATED_MUSIC_TITLE)
            .musicPath(UPDATED_MUSIC_PATH)
            .vbEnabled(UPDATED_VB_ENABLED)
            .useSwitch(UPDATED_USE_SWITCH)
            .createdDate(UPDATED_CREATED_DATE)
            .crawlingDate(UPDATED_CRAWLING_DATE);

        restAlarmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlarm.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlarm))
            )
            .andExpect(status().isOk());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
        Alarm testAlarm = alarmList.get(alarmList.size() - 1);
        assertThat(testAlarm.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAlarm.getSiteUrl()).isEqualTo(UPDATED_SITE_URL);
        assertThat(testAlarm.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlarm.getRefeshTime()).isEqualTo(UPDATED_REFESH_TIME);
        assertThat(testAlarm.getVbEnabled()).isEqualTo(UPDATED_VB_ENABLED);
        assertThat(testAlarm.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAlarm.getCrawlingDate()).isEqualTo(UPDATED_CRAWLING_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAlarm() throws Exception {
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();
        alarm.setId(count.incrementAndGet());

        // Create the Alarm
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlarmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alarmDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlarm() throws Exception {
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();
        alarm.setId(count.incrementAndGet());

        // Create the Alarm
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlarmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlarm() throws Exception {
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();
        alarm.setId(count.incrementAndGet());

        // Create the Alarm
        AlarmDTO alarmDTO = alarmMapper.toDto(alarm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlarmMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alarmDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlarm() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);

        int databaseSizeBeforeDelete = alarmRepository.findAll().size();

        // Delete the alarm
        restAlarmMockMvc
            .perform(delete(ENTITY_API_URL_ID, alarm.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
