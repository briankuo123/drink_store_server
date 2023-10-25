package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Coupon;
import com.mycompany.myapp.repository.CouponRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link CouponResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CouponResourceIT {

    private static final UUID DEFAULT_COUPON_ID = UUID.randomUUID();
    private static final UUID UPDATED_COUPON_ID = UUID.randomUUID();

    private static final String DEFAULT_COUPON_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUPON_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUPON_VALUE = 1;
    private static final Integer UPDATED_COUPON_VALUE = 2;

    private static final Integer DEFAULT_COUPON_USE_TIMES = 1;
    private static final Integer UPDATED_COUPON_USE_TIMES = 2;

    private static final Instant DEFAULT_COUPON_EXPIRE_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COUPON_EXPIRE_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/coupons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCouponMockMvc;

    private Coupon coupon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coupon createEntity(EntityManager em) {
        Coupon coupon = new Coupon()
            .couponId(DEFAULT_COUPON_ID)
            .couponCode(DEFAULT_COUPON_CODE)
            .couponValue(DEFAULT_COUPON_VALUE)
            .couponUseTimes(DEFAULT_COUPON_USE_TIMES)
            .couponExpireDatetime(DEFAULT_COUPON_EXPIRE_DATETIME)
            .createBy(DEFAULT_CREATE_BY)
            .createDatetime(DEFAULT_CREATE_DATETIME)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDatetime(DEFAULT_LAST_MODIFIED_DATETIME);
        return coupon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coupon createUpdatedEntity(EntityManager em) {
        Coupon coupon = new Coupon()
            .couponId(UPDATED_COUPON_ID)
            .couponCode(UPDATED_COUPON_CODE)
            .couponValue(UPDATED_COUPON_VALUE)
            .couponUseTimes(UPDATED_COUPON_USE_TIMES)
            .couponExpireDatetime(UPDATED_COUPON_EXPIRE_DATETIME)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);
        return coupon;
    }

    @BeforeEach
    public void initTest() {
        coupon = createEntity(em);
    }

    @Test
    @Transactional
    void createCoupon() throws Exception {
        int databaseSizeBeforeCreate = couponRepository.findAll().size();
        // Create the Coupon
        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coupon)))
            .andExpect(status().isCreated());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeCreate + 1);
        Coupon testCoupon = couponList.get(couponList.size() - 1);
        assertThat(testCoupon.getCouponId()).isEqualTo(DEFAULT_COUPON_ID);
        assertThat(testCoupon.getCouponCode()).isEqualTo(DEFAULT_COUPON_CODE);
        assertThat(testCoupon.getCouponValue()).isEqualTo(DEFAULT_COUPON_VALUE);
        assertThat(testCoupon.getCouponUseTimes()).isEqualTo(DEFAULT_COUPON_USE_TIMES);
        assertThat(testCoupon.getCouponExpireDatetime()).isEqualTo(DEFAULT_COUPON_EXPIRE_DATETIME);
        assertThat(testCoupon.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testCoupon.getCreateDatetime()).isEqualTo(DEFAULT_CREATE_DATETIME);
        assertThat(testCoupon.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCoupon.getLastModifiedDatetime()).isEqualTo(DEFAULT_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void createCouponWithExistingId() throws Exception {
        // Create the Coupon with an existing ID
        coupon.setId(1L);

        int databaseSizeBeforeCreate = couponRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coupon)))
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCouponIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = couponRepository.findAll().size();
        // set the field null
        coupon.setCouponId(null);

        // Create the Coupon, which fails.

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coupon)))
            .andExpect(status().isBadRequest());

        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCouponCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = couponRepository.findAll().size();
        // set the field null
        coupon.setCouponCode(null);

        // Create the Coupon, which fails.

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coupon)))
            .andExpect(status().isBadRequest());

        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCouponValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = couponRepository.findAll().size();
        // set the field null
        coupon.setCouponValue(null);

        // Create the Coupon, which fails.

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coupon)))
            .andExpect(status().isBadRequest());

        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoupons() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

        // Get all the couponList
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coupon.getId().intValue())))
            .andExpect(jsonPath("$.[*].couponId").value(hasItem(DEFAULT_COUPON_ID.toString())))
            .andExpect(jsonPath("$.[*].couponCode").value(hasItem(DEFAULT_COUPON_CODE)))
            .andExpect(jsonPath("$.[*].couponValue").value(hasItem(DEFAULT_COUPON_VALUE)))
            .andExpect(jsonPath("$.[*].couponUseTimes").value(hasItem(DEFAULT_COUPON_USE_TIMES)))
            .andExpect(jsonPath("$.[*].couponExpireDatetime").value(hasItem(DEFAULT_COUPON_EXPIRE_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createDatetime").value(hasItem(DEFAULT_CREATE_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDatetime").value(hasItem(DEFAULT_LAST_MODIFIED_DATETIME.toString())));
    }

    @Test
    @Transactional
    void getCoupon() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

        // Get the coupon
        restCouponMockMvc
            .perform(get(ENTITY_API_URL_ID, coupon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coupon.getId().intValue()))
            .andExpect(jsonPath("$.couponId").value(DEFAULT_COUPON_ID.toString()))
            .andExpect(jsonPath("$.couponCode").value(DEFAULT_COUPON_CODE))
            .andExpect(jsonPath("$.couponValue").value(DEFAULT_COUPON_VALUE))
            .andExpect(jsonPath("$.couponUseTimes").value(DEFAULT_COUPON_USE_TIMES))
            .andExpect(jsonPath("$.couponExpireDatetime").value(DEFAULT_COUPON_EXPIRE_DATETIME.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.createDatetime").value(DEFAULT_CREATE_DATETIME.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDatetime").value(DEFAULT_LAST_MODIFIED_DATETIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCoupon() throws Exception {
        // Get the coupon
        restCouponMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoupon() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

        int databaseSizeBeforeUpdate = couponRepository.findAll().size();

        // Update the coupon
        Coupon updatedCoupon = couponRepository.findById(coupon.getId()).get();
        // Disconnect from session so that the updates on updatedCoupon are not directly saved in db
        em.detach(updatedCoupon);
        updatedCoupon
            .couponId(UPDATED_COUPON_ID)
            .couponCode(UPDATED_COUPON_CODE)
            .couponValue(UPDATED_COUPON_VALUE)
            .couponUseTimes(UPDATED_COUPON_USE_TIMES)
            .couponExpireDatetime(UPDATED_COUPON_EXPIRE_DATETIME)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);

        restCouponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCoupon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCoupon))
            )
            .andExpect(status().isOk());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeUpdate);
        Coupon testCoupon = couponList.get(couponList.size() - 1);
        assertThat(testCoupon.getCouponId()).isEqualTo(UPDATED_COUPON_ID);
        assertThat(testCoupon.getCouponCode()).isEqualTo(UPDATED_COUPON_CODE);
        assertThat(testCoupon.getCouponValue()).isEqualTo(UPDATED_COUPON_VALUE);
        assertThat(testCoupon.getCouponUseTimes()).isEqualTo(UPDATED_COUPON_USE_TIMES);
        assertThat(testCoupon.getCouponExpireDatetime()).isEqualTo(UPDATED_COUPON_EXPIRE_DATETIME);
        assertThat(testCoupon.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testCoupon.getCreateDatetime()).isEqualTo(UPDATED_CREATE_DATETIME);
        assertThat(testCoupon.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCoupon.getLastModifiedDatetime()).isEqualTo(UPDATED_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void putNonExistingCoupon() throws Exception {
        int databaseSizeBeforeUpdate = couponRepository.findAll().size();
        coupon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coupon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coupon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoupon() throws Exception {
        int databaseSizeBeforeUpdate = couponRepository.findAll().size();
        coupon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coupon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoupon() throws Exception {
        int databaseSizeBeforeUpdate = couponRepository.findAll().size();
        coupon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coupon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCouponWithPatch() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

        int databaseSizeBeforeUpdate = couponRepository.findAll().size();

        // Update the coupon using partial update
        Coupon partialUpdatedCoupon = new Coupon();
        partialUpdatedCoupon.setId(coupon.getId());

        partialUpdatedCoupon
            .couponId(UPDATED_COUPON_ID)
            .couponCode(UPDATED_COUPON_CODE)
            .couponUseTimes(UPDATED_COUPON_USE_TIMES)
            .couponExpireDatetime(UPDATED_COUPON_EXPIRE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);

        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoupon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoupon))
            )
            .andExpect(status().isOk());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeUpdate);
        Coupon testCoupon = couponList.get(couponList.size() - 1);
        assertThat(testCoupon.getCouponId()).isEqualTo(UPDATED_COUPON_ID);
        assertThat(testCoupon.getCouponCode()).isEqualTo(UPDATED_COUPON_CODE);
        assertThat(testCoupon.getCouponValue()).isEqualTo(DEFAULT_COUPON_VALUE);
        assertThat(testCoupon.getCouponUseTimes()).isEqualTo(UPDATED_COUPON_USE_TIMES);
        assertThat(testCoupon.getCouponExpireDatetime()).isEqualTo(UPDATED_COUPON_EXPIRE_DATETIME);
        assertThat(testCoupon.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testCoupon.getCreateDatetime()).isEqualTo(DEFAULT_CREATE_DATETIME);
        assertThat(testCoupon.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCoupon.getLastModifiedDatetime()).isEqualTo(UPDATED_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void fullUpdateCouponWithPatch() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

        int databaseSizeBeforeUpdate = couponRepository.findAll().size();

        // Update the coupon using partial update
        Coupon partialUpdatedCoupon = new Coupon();
        partialUpdatedCoupon.setId(coupon.getId());

        partialUpdatedCoupon
            .couponId(UPDATED_COUPON_ID)
            .couponCode(UPDATED_COUPON_CODE)
            .couponValue(UPDATED_COUPON_VALUE)
            .couponUseTimes(UPDATED_COUPON_USE_TIMES)
            .couponExpireDatetime(UPDATED_COUPON_EXPIRE_DATETIME)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);

        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoupon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoupon))
            )
            .andExpect(status().isOk());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeUpdate);
        Coupon testCoupon = couponList.get(couponList.size() - 1);
        assertThat(testCoupon.getCouponId()).isEqualTo(UPDATED_COUPON_ID);
        assertThat(testCoupon.getCouponCode()).isEqualTo(UPDATED_COUPON_CODE);
        assertThat(testCoupon.getCouponValue()).isEqualTo(UPDATED_COUPON_VALUE);
        assertThat(testCoupon.getCouponUseTimes()).isEqualTo(UPDATED_COUPON_USE_TIMES);
        assertThat(testCoupon.getCouponExpireDatetime()).isEqualTo(UPDATED_COUPON_EXPIRE_DATETIME);
        assertThat(testCoupon.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testCoupon.getCreateDatetime()).isEqualTo(UPDATED_CREATE_DATETIME);
        assertThat(testCoupon.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCoupon.getLastModifiedDatetime()).isEqualTo(UPDATED_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void patchNonExistingCoupon() throws Exception {
        int databaseSizeBeforeUpdate = couponRepository.findAll().size();
        coupon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coupon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coupon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoupon() throws Exception {
        int databaseSizeBeforeUpdate = couponRepository.findAll().size();
        coupon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coupon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoupon() throws Exception {
        int databaseSizeBeforeUpdate = couponRepository.findAll().size();
        coupon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coupon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coupon in the database
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoupon() throws Exception {
        // Initialize the database
        couponRepository.saveAndFlush(coupon);

        int databaseSizeBeforeDelete = couponRepository.findAll().size();

        // Delete the coupon
        restCouponMockMvc
            .perform(delete(ENTITY_API_URL_ID, coupon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Coupon> couponList = couponRepository.findAll();
        assertThat(couponList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
