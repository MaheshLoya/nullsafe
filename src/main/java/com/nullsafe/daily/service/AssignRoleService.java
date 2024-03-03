package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.AssignRole;
import com.nullsafe.daily.repository.AssignRoleRepository;
import com.nullsafe.daily.service.dto.AssignRoleDTO;
import com.nullsafe.daily.service.mapper.AssignRoleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.AssignRole}.
 */
@Service
@Transactional
public class AssignRoleService {

    private final Logger log = LoggerFactory.getLogger(AssignRoleService.class);

    private final AssignRoleRepository assignRoleRepository;

    private final AssignRoleMapper assignRoleMapper;

    public AssignRoleService(AssignRoleRepository assignRoleRepository, AssignRoleMapper assignRoleMapper) {
        this.assignRoleRepository = assignRoleRepository;
        this.assignRoleMapper = assignRoleMapper;
    }

    /**
     * Save a assignRole.
     *
     * @param assignRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public AssignRoleDTO save(AssignRoleDTO assignRoleDTO) {
        log.debug("Request to save AssignRole : {}", assignRoleDTO);
        AssignRole assignRole = assignRoleMapper.toEntity(assignRoleDTO);
        assignRole = assignRoleRepository.save(assignRole);
        return assignRoleMapper.toDto(assignRole);
    }

    /**
     * Update a assignRole.
     *
     * @param assignRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public AssignRoleDTO update(AssignRoleDTO assignRoleDTO) {
        log.debug("Request to update AssignRole : {}", assignRoleDTO);
        AssignRole assignRole = assignRoleMapper.toEntity(assignRoleDTO);
        assignRole = assignRoleRepository.save(assignRole);
        return assignRoleMapper.toDto(assignRole);
    }

    /**
     * Partially update a assignRole.
     *
     * @param assignRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssignRoleDTO> partialUpdate(AssignRoleDTO assignRoleDTO) {
        log.debug("Request to partially update AssignRole : {}", assignRoleDTO);

        return assignRoleRepository
            .findById(assignRoleDTO.getId())
            .map(existingAssignRole -> {
                assignRoleMapper.partialUpdate(existingAssignRole, assignRoleDTO);

                return existingAssignRole;
            })
            .map(assignRoleRepository::save)
            .map(assignRoleMapper::toDto);
    }

    /**
     * Get all the assignRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssignRoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssignRoles");
        return assignRoleRepository.findAll(pageable).map(assignRoleMapper::toDto);
    }

    /**
     * Get all the assignRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AssignRoleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assignRoleRepository.findAllWithEagerRelationships(pageable).map(assignRoleMapper::toDto);
    }

    /**
     * Get one assignRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssignRoleDTO> findOne(Long id) {
        log.debug("Request to get AssignRole : {}", id);
        return assignRoleRepository.findOneWithEagerRelationships(id).map(assignRoleMapper::toDto);
    }

    /**
     * Delete the assignRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AssignRole : {}", id);
        assignRoleRepository.deleteById(id);
    }
}
