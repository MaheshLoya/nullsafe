package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.AssignRole;
import com.nullsafe.daily.domain.Role;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.AssignRoleRepository;
import com.nullsafe.daily.service.AssignRoleService;
import com.nullsafe.daily.service.dto.AssignRoleDTO;
import com.nullsafe.daily.service.mapper.AssignRoleMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AssignRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssignRoleResourceIT {

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/assign-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssignRoleRepository assignRoleRepository;

    @Mock
    private AssignRoleRepository assignRoleRepositoryMock;

    @Autowired
    private AssignRoleMapper assignRoleMapper;

    @Mock
    private AssignRoleService assignRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssignRoleMockMvc;

    private AssignRole assignRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssignRole createEntity(EntityManager em) {
        AssignRole assignRole = new AssignRole().createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        assignRole.setUser(users);
        // Add required entity
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            role = RoleResourceIT.createEntity(em);
            em.persist(role);
            em.flush();
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        assignRole.setRole(role);
        return assignRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssignRole createUpdatedEntity(EntityManager em) {
        AssignRole assignRole = new AssignRole().createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createUpdatedEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        assignRole.setUser(users);
        // Add required entity
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            role = RoleResourceIT.createUpdatedEntity(em);
            em.persist(role);
            em.flush();
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        assignRole.setRole(role);
        return assignRole;
    }

    @BeforeEach
    public void initTest() {
        assignRole = createEntity(em);
    }

    @Test
    @Transactional
    void createAssignRole() throws Exception {
        int databaseSizeBeforeCreate = assignRoleRepository.findAll().size();
        // Create the AssignRole
        AssignRoleDTO assignRoleDTO = assignRoleMapper.toDto(assignRole);
        restAssignRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignRoleDTO)))
            .andExpect(status().isCreated());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeCreate + 1);
        AssignRole testAssignRole = assignRoleList.get(assignRoleList.size() - 1);
        assertThat(testAssignRole.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAssignRole.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createAssignRoleWithExistingId() throws Exception {
        // Create the AssignRole with an existing ID
        assignRole.setId(1L);
        AssignRoleDTO assignRoleDTO = assignRoleMapper.toDto(assignRole);

        int databaseSizeBeforeCreate = assignRoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssignRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignRoleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssignRoles() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        // Get all the assignRoleList
        restAssignRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssignRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(assignRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssignRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assignRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssignRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assignRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssignRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(assignRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAssignRole() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        // Get the assignRole
        restAssignRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, assignRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assignRole.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getAssignRolesByIdFiltering() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        Long id = assignRole.getId();

        defaultAssignRoleShouldBeFound("id.equals=" + id);
        defaultAssignRoleShouldNotBeFound("id.notEquals=" + id);

        defaultAssignRoleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssignRoleShouldNotBeFound("id.greaterThan=" + id);

        defaultAssignRoleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssignRoleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssignRolesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        // Get all the assignRoleList where createdAt equals to DEFAULT_CREATED_AT
        defaultAssignRoleShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the assignRoleList where createdAt equals to UPDATED_CREATED_AT
        defaultAssignRoleShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAssignRolesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        // Get all the assignRoleList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAssignRoleShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the assignRoleList where createdAt equals to UPDATED_CREATED_AT
        defaultAssignRoleShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAssignRolesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        // Get all the assignRoleList where createdAt is not null
        defaultAssignRoleShouldBeFound("createdAt.specified=true");

        // Get all the assignRoleList where createdAt is null
        defaultAssignRoleShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignRolesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        // Get all the assignRoleList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultAssignRoleShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the assignRoleList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAssignRoleShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAssignRolesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        // Get all the assignRoleList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultAssignRoleShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the assignRoleList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAssignRoleShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAssignRolesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        // Get all the assignRoleList where updatedAt is not null
        defaultAssignRoleShouldBeFound("updatedAt.specified=true");

        // Get all the assignRoleList where updatedAt is null
        defaultAssignRoleShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignRolesByUserIsEqualToSomething() throws Exception {
        Users user;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            assignRoleRepository.saveAndFlush(assignRole);
            user = UsersResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(user);
        em.flush();
        assignRole.setUser(user);
        assignRoleRepository.saveAndFlush(assignRole);
        Long userId = user.getId();
        // Get all the assignRoleList where user equals to userId
        defaultAssignRoleShouldBeFound("userId.equals=" + userId);

        // Get all the assignRoleList where user equals to (userId + 1)
        defaultAssignRoleShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllAssignRolesByRoleIsEqualToSomething() throws Exception {
        Role role;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            assignRoleRepository.saveAndFlush(assignRole);
            role = RoleResourceIT.createEntity(em);
        } else {
            role = TestUtil.findAll(em, Role.class).get(0);
        }
        em.persist(role);
        em.flush();
        assignRole.setRole(role);
        assignRoleRepository.saveAndFlush(assignRole);
        Long roleId = role.getId();
        // Get all the assignRoleList where role equals to roleId
        defaultAssignRoleShouldBeFound("roleId.equals=" + roleId);

        // Get all the assignRoleList where role equals to (roleId + 1)
        defaultAssignRoleShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssignRoleShouldBeFound(String filter) throws Exception {
        restAssignRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restAssignRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssignRoleShouldNotBeFound(String filter) throws Exception {
        restAssignRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssignRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssignRole() throws Exception {
        // Get the assignRole
        restAssignRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssignRole() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        int databaseSizeBeforeUpdate = assignRoleRepository.findAll().size();

        // Update the assignRole
        AssignRole updatedAssignRole = assignRoleRepository.findById(assignRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAssignRole are not directly saved in db
        em.detach(updatedAssignRole);
        updatedAssignRole.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        AssignRoleDTO assignRoleDTO = assignRoleMapper.toDto(updatedAssignRole);

        restAssignRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assignRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assignRoleDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeUpdate);
        AssignRole testAssignRole = assignRoleList.get(assignRoleList.size() - 1);
        assertThat(testAssignRole.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAssignRole.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAssignRole() throws Exception {
        int databaseSizeBeforeUpdate = assignRoleRepository.findAll().size();
        assignRole.setId(longCount.incrementAndGet());

        // Create the AssignRole
        AssignRoleDTO assignRoleDTO = assignRoleMapper.toDto(assignRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assignRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assignRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssignRole() throws Exception {
        int databaseSizeBeforeUpdate = assignRoleRepository.findAll().size();
        assignRole.setId(longCount.incrementAndGet());

        // Create the AssignRole
        AssignRoleDTO assignRoleDTO = assignRoleMapper.toDto(assignRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assignRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssignRole() throws Exception {
        int databaseSizeBeforeUpdate = assignRoleRepository.findAll().size();
        assignRole.setId(longCount.incrementAndGet());

        // Create the AssignRole
        AssignRoleDTO assignRoleDTO = assignRoleMapper.toDto(assignRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssignRoleWithPatch() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        int databaseSizeBeforeUpdate = assignRoleRepository.findAll().size();

        // Update the assignRole using partial update
        AssignRole partialUpdatedAssignRole = new AssignRole();
        partialUpdatedAssignRole.setId(assignRole.getId());

        partialUpdatedAssignRole.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restAssignRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssignRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssignRole))
            )
            .andExpect(status().isOk());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeUpdate);
        AssignRole testAssignRole = assignRoleList.get(assignRoleList.size() - 1);
        assertThat(testAssignRole.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAssignRole.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAssignRoleWithPatch() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        int databaseSizeBeforeUpdate = assignRoleRepository.findAll().size();

        // Update the assignRole using partial update
        AssignRole partialUpdatedAssignRole = new AssignRole();
        partialUpdatedAssignRole.setId(assignRole.getId());

        partialUpdatedAssignRole.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restAssignRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssignRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssignRole))
            )
            .andExpect(status().isOk());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeUpdate);
        AssignRole testAssignRole = assignRoleList.get(assignRoleList.size() - 1);
        assertThat(testAssignRole.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAssignRole.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAssignRole() throws Exception {
        int databaseSizeBeforeUpdate = assignRoleRepository.findAll().size();
        assignRole.setId(longCount.incrementAndGet());

        // Create the AssignRole
        AssignRoleDTO assignRoleDTO = assignRoleMapper.toDto(assignRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assignRoleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assignRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssignRole() throws Exception {
        int databaseSizeBeforeUpdate = assignRoleRepository.findAll().size();
        assignRole.setId(longCount.incrementAndGet());

        // Create the AssignRole
        AssignRoleDTO assignRoleDTO = assignRoleMapper.toDto(assignRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assignRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssignRole() throws Exception {
        int databaseSizeBeforeUpdate = assignRoleRepository.findAll().size();
        assignRole.setId(longCount.incrementAndGet());

        // Create the AssignRole
        AssignRoleDTO assignRoleDTO = assignRoleMapper.toDto(assignRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignRoleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assignRoleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssignRole in the database
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssignRole() throws Exception {
        // Initialize the database
        assignRoleRepository.saveAndFlush(assignRole);

        int databaseSizeBeforeDelete = assignRoleRepository.findAll().size();

        // Delete the assignRole
        restAssignRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, assignRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssignRole> assignRoleList = assignRoleRepository.findAll();
        assertThat(assignRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
