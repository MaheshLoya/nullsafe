package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.Migrations;
import com.nullsafe.daily.repository.MigrationsRepository;
import com.nullsafe.daily.service.dto.MigrationsDTO;
import com.nullsafe.daily.service.mapper.MigrationsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.Migrations}.
 */
@Service
@Transactional
public class MigrationsService {

    private final Logger log = LoggerFactory.getLogger(MigrationsService.class);

    private final MigrationsRepository migrationsRepository;

    private final MigrationsMapper migrationsMapper;

    public MigrationsService(MigrationsRepository migrationsRepository, MigrationsMapper migrationsMapper) {
        this.migrationsRepository = migrationsRepository;
        this.migrationsMapper = migrationsMapper;
    }

    /**
     * Save a migrations.
     *
     * @param migrationsDTO the entity to save.
     * @return the persisted entity.
     */
    public MigrationsDTO save(MigrationsDTO migrationsDTO) {
        log.debug("Request to save Migrations : {}", migrationsDTO);
        Migrations migrations = migrationsMapper.toEntity(migrationsDTO);
        migrations = migrationsRepository.save(migrations);
        return migrationsMapper.toDto(migrations);
    }

    /**
     * Update a migrations.
     *
     * @param migrationsDTO the entity to save.
     * @return the persisted entity.
     */
    public MigrationsDTO update(MigrationsDTO migrationsDTO) {
        log.debug("Request to update Migrations : {}", migrationsDTO);
        Migrations migrations = migrationsMapper.toEntity(migrationsDTO);
        migrations = migrationsRepository.save(migrations);
        return migrationsMapper.toDto(migrations);
    }

    /**
     * Partially update a migrations.
     *
     * @param migrationsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MigrationsDTO> partialUpdate(MigrationsDTO migrationsDTO) {
        log.debug("Request to partially update Migrations : {}", migrationsDTO);

        return migrationsRepository
            .findById(migrationsDTO.getId())
            .map(existingMigrations -> {
                migrationsMapper.partialUpdate(existingMigrations, migrationsDTO);

                return existingMigrations;
            })
            .map(migrationsRepository::save)
            .map(migrationsMapper::toDto);
    }

    /**
     * Get all the migrations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MigrationsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Migrations");
        return migrationsRepository.findAll(pageable).map(migrationsMapper::toDto);
    }

    /**
     * Get one migrations by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MigrationsDTO> findOne(Integer id) {
        log.debug("Request to get Migrations : {}", id);
        return migrationsRepository.findById(id).map(migrationsMapper::toDto);
    }

    /**
     * Delete the migrations by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Integer id) {
        log.debug("Request to delete Migrations : {}", id);
        migrationsRepository.deleteById(id);
    }
}
