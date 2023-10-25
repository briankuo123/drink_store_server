package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Menu;
import com.mycompany.myapp.repository.MenuRepository;
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
 * Integration tests for the {@link MenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuResourceIT {

    private static final UUID DEFAULT_DRINK_ID = UUID.randomUUID();
    private static final UUID UPDATED_DRINK_ID = UUID.randomUUID();

    private static final String DEFAULT_DRINK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DRINK_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SUGAR = false;
    private static final Boolean UPDATED_SUGAR = true;

    private static final Boolean DEFAULT_ICE = false;
    private static final Boolean UPDATED_ICE = true;

    private static final Boolean DEFAULT_HOT = false;
    private static final Boolean UPDATED_HOT = true;

    private static final String DEFAULT_TOPPINGS = "AAAAAAAAAA";
    private static final String UPDATED_TOPPINGS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DRINK_SIZE = false;
    private static final Boolean UPDATED_DRINK_SIZE = true;

    private static final Integer DEFAULT_DRINK_PRICE = 1;
    private static final Integer UPDATED_DRINK_PRICE = 2;

    private static final String DEFAULT_DRINK_PICTURE_URL = "AAAAAAAAAA";
    private static final String UPDATED_DRINK_PICTURE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuMockMvc;

    private Menu menu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createEntity(EntityManager em) {
        Menu menu = new Menu()
            .drinkId(DEFAULT_DRINK_ID)
            .drinkName(DEFAULT_DRINK_NAME)
            .sugar(DEFAULT_SUGAR)
            .ice(DEFAULT_ICE)
            .hot(DEFAULT_HOT)
            .toppings(DEFAULT_TOPPINGS)
            .drinkSize(DEFAULT_DRINK_SIZE)
            .drinkPrice(DEFAULT_DRINK_PRICE)
            .drinkPictureURL(DEFAULT_DRINK_PICTURE_URL)
            .createBy(DEFAULT_CREATE_BY)
            .createDatetime(DEFAULT_CREATE_DATETIME)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDatetime(DEFAULT_LAST_MODIFIED_DATETIME);
        return menu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createUpdatedEntity(EntityManager em) {
        Menu menu = new Menu()
            .drinkId(UPDATED_DRINK_ID)
            .drinkName(UPDATED_DRINK_NAME)
            .sugar(UPDATED_SUGAR)
            .ice(UPDATED_ICE)
            .hot(UPDATED_HOT)
            .toppings(UPDATED_TOPPINGS)
            .drinkSize(UPDATED_DRINK_SIZE)
            .drinkPrice(UPDATED_DRINK_PRICE)
            .drinkPictureURL(UPDATED_DRINK_PICTURE_URL)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);
        return menu;
    }

    @BeforeEach
    public void initTest() {
        menu = createEntity(em);
    }

    @Test
    @Transactional
    void createMenu() throws Exception {
        int databaseSizeBeforeCreate = menuRepository.findAll().size();
        // Create the Menu
        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isCreated());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate + 1);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getDrinkId()).isEqualTo(DEFAULT_DRINK_ID);
        assertThat(testMenu.getDrinkName()).isEqualTo(DEFAULT_DRINK_NAME);
        assertThat(testMenu.getSugar()).isEqualTo(DEFAULT_SUGAR);
        assertThat(testMenu.getIce()).isEqualTo(DEFAULT_ICE);
        assertThat(testMenu.getHot()).isEqualTo(DEFAULT_HOT);
        assertThat(testMenu.getToppings()).isEqualTo(DEFAULT_TOPPINGS);
        assertThat(testMenu.getDrinkSize()).isEqualTo(DEFAULT_DRINK_SIZE);
        assertThat(testMenu.getDrinkPrice()).isEqualTo(DEFAULT_DRINK_PRICE);
        assertThat(testMenu.getDrinkPictureURL()).isEqualTo(DEFAULT_DRINK_PICTURE_URL);
        assertThat(testMenu.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testMenu.getCreateDatetime()).isEqualTo(DEFAULT_CREATE_DATETIME);
        assertThat(testMenu.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testMenu.getLastModifiedDatetime()).isEqualTo(DEFAULT_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void createMenuWithExistingId() throws Exception {
        // Create the Menu with an existing ID
        menu.setId(1L);

        int databaseSizeBeforeCreate = menuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDrinkIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setDrinkId(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDrinkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setDrinkName(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSugarIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setSugar(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIceIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setIce(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHotIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setHot(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDrinkSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setDrinkSize(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDrinkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setDrinkPrice(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMenus() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList
        restMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menu.getId().intValue())))
            .andExpect(jsonPath("$.[*].drinkId").value(hasItem(DEFAULT_DRINK_ID.toString())))
            .andExpect(jsonPath("$.[*].drinkName").value(hasItem(DEFAULT_DRINK_NAME)))
            .andExpect(jsonPath("$.[*].sugar").value(hasItem(DEFAULT_SUGAR.booleanValue())))
            .andExpect(jsonPath("$.[*].ice").value(hasItem(DEFAULT_ICE.booleanValue())))
            .andExpect(jsonPath("$.[*].hot").value(hasItem(DEFAULT_HOT.booleanValue())))
            .andExpect(jsonPath("$.[*].toppings").value(hasItem(DEFAULT_TOPPINGS)))
            .andExpect(jsonPath("$.[*].drinkSize").value(hasItem(DEFAULT_DRINK_SIZE.booleanValue())))
            .andExpect(jsonPath("$.[*].drinkPrice").value(hasItem(DEFAULT_DRINK_PRICE)))
            .andExpect(jsonPath("$.[*].drinkPictureURL").value(hasItem(DEFAULT_DRINK_PICTURE_URL)))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createDatetime").value(hasItem(DEFAULT_CREATE_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDatetime").value(hasItem(DEFAULT_LAST_MODIFIED_DATETIME.toString())));
    }

    @Test
    @Transactional
    void getMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get the menu
        restMenuMockMvc
            .perform(get(ENTITY_API_URL_ID, menu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menu.getId().intValue()))
            .andExpect(jsonPath("$.drinkId").value(DEFAULT_DRINK_ID.toString()))
            .andExpect(jsonPath("$.drinkName").value(DEFAULT_DRINK_NAME))
            .andExpect(jsonPath("$.sugar").value(DEFAULT_SUGAR.booleanValue()))
            .andExpect(jsonPath("$.ice").value(DEFAULT_ICE.booleanValue()))
            .andExpect(jsonPath("$.hot").value(DEFAULT_HOT.booleanValue()))
            .andExpect(jsonPath("$.toppings").value(DEFAULT_TOPPINGS))
            .andExpect(jsonPath("$.drinkSize").value(DEFAULT_DRINK_SIZE.booleanValue()))
            .andExpect(jsonPath("$.drinkPrice").value(DEFAULT_DRINK_PRICE))
            .andExpect(jsonPath("$.drinkPictureURL").value(DEFAULT_DRINK_PICTURE_URL))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.createDatetime").value(DEFAULT_CREATE_DATETIME.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDatetime").value(DEFAULT_LAST_MODIFIED_DATETIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMenu() throws Exception {
        // Get the menu
        restMenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu
        Menu updatedMenu = menuRepository.findById(menu.getId()).get();
        // Disconnect from session so that the updates on updatedMenu are not directly saved in db
        em.detach(updatedMenu);
        updatedMenu
            .drinkId(UPDATED_DRINK_ID)
            .drinkName(UPDATED_DRINK_NAME)
            .sugar(UPDATED_SUGAR)
            .ice(UPDATED_ICE)
            .hot(UPDATED_HOT)
            .toppings(UPDATED_TOPPINGS)
            .drinkSize(UPDATED_DRINK_SIZE)
            .drinkPrice(UPDATED_DRINK_PRICE)
            .drinkPictureURL(UPDATED_DRINK_PICTURE_URL)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);

        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getDrinkId()).isEqualTo(UPDATED_DRINK_ID);
        assertThat(testMenu.getDrinkName()).isEqualTo(UPDATED_DRINK_NAME);
        assertThat(testMenu.getSugar()).isEqualTo(UPDATED_SUGAR);
        assertThat(testMenu.getIce()).isEqualTo(UPDATED_ICE);
        assertThat(testMenu.getHot()).isEqualTo(UPDATED_HOT);
        assertThat(testMenu.getToppings()).isEqualTo(UPDATED_TOPPINGS);
        assertThat(testMenu.getDrinkSize()).isEqualTo(UPDATED_DRINK_SIZE);
        assertThat(testMenu.getDrinkPrice()).isEqualTo(UPDATED_DRINK_PRICE);
        assertThat(testMenu.getDrinkPictureURL()).isEqualTo(UPDATED_DRINK_PICTURE_URL);
        assertThat(testMenu.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testMenu.getCreateDatetime()).isEqualTo(UPDATED_CREATE_DATETIME);
        assertThat(testMenu.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMenu.getLastModifiedDatetime()).isEqualTo(UPDATED_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void putNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu
            .ice(UPDATED_ICE)
            .drinkPictureURL(UPDATED_DRINK_PICTURE_URL)
            .createBy(UPDATED_CREATE_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getDrinkId()).isEqualTo(DEFAULT_DRINK_ID);
        assertThat(testMenu.getDrinkName()).isEqualTo(DEFAULT_DRINK_NAME);
        assertThat(testMenu.getSugar()).isEqualTo(DEFAULT_SUGAR);
        assertThat(testMenu.getIce()).isEqualTo(UPDATED_ICE);
        assertThat(testMenu.getHot()).isEqualTo(DEFAULT_HOT);
        assertThat(testMenu.getToppings()).isEqualTo(DEFAULT_TOPPINGS);
        assertThat(testMenu.getDrinkSize()).isEqualTo(DEFAULT_DRINK_SIZE);
        assertThat(testMenu.getDrinkPrice()).isEqualTo(DEFAULT_DRINK_PRICE);
        assertThat(testMenu.getDrinkPictureURL()).isEqualTo(UPDATED_DRINK_PICTURE_URL);
        assertThat(testMenu.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testMenu.getCreateDatetime()).isEqualTo(DEFAULT_CREATE_DATETIME);
        assertThat(testMenu.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMenu.getLastModifiedDatetime()).isEqualTo(UPDATED_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void fullUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu
            .drinkId(UPDATED_DRINK_ID)
            .drinkName(UPDATED_DRINK_NAME)
            .sugar(UPDATED_SUGAR)
            .ice(UPDATED_ICE)
            .hot(UPDATED_HOT)
            .toppings(UPDATED_TOPPINGS)
            .drinkSize(UPDATED_DRINK_SIZE)
            .drinkPrice(UPDATED_DRINK_PRICE)
            .drinkPictureURL(UPDATED_DRINK_PICTURE_URL)
            .createBy(UPDATED_CREATE_BY)
            .createDatetime(UPDATED_CREATE_DATETIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDatetime(UPDATED_LAST_MODIFIED_DATETIME);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getDrinkId()).isEqualTo(UPDATED_DRINK_ID);
        assertThat(testMenu.getDrinkName()).isEqualTo(UPDATED_DRINK_NAME);
        assertThat(testMenu.getSugar()).isEqualTo(UPDATED_SUGAR);
        assertThat(testMenu.getIce()).isEqualTo(UPDATED_ICE);
        assertThat(testMenu.getHot()).isEqualTo(UPDATED_HOT);
        assertThat(testMenu.getToppings()).isEqualTo(UPDATED_TOPPINGS);
        assertThat(testMenu.getDrinkSize()).isEqualTo(UPDATED_DRINK_SIZE);
        assertThat(testMenu.getDrinkPrice()).isEqualTo(UPDATED_DRINK_PRICE);
        assertThat(testMenu.getDrinkPictureURL()).isEqualTo(UPDATED_DRINK_PICTURE_URL);
        assertThat(testMenu.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testMenu.getCreateDatetime()).isEqualTo(UPDATED_CREATE_DATETIME);
        assertThat(testMenu.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMenu.getLastModifiedDatetime()).isEqualTo(UPDATED_LAST_MODIFIED_DATETIME);
    }

    @Test
    @Transactional
    void patchNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeDelete = menuRepository.findAll().size();

        // Delete the menu
        restMenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, menu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
