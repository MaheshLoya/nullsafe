package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.AppSettings;
import com.nullsafe.daily.repository.AppSettingsRepository;
import com.nullsafe.daily.service.dto.AppSettingsDTO;
import com.nullsafe.daily.service.mapper.AppSettingsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.AppSettings}.
 */
@Service
@Transactional
public class AppSettingsService {

    private final Logger log = LoggerFactory.getLogger(AppSettingsService.class);

    private final AppSettingsRepository appSettingsRepository;

    private final AppSettingsMapper appSettingsMapper;

    public AppSettingsService(AppSettingsRepository appSettingsRepository, AppSettingsMapper appSettingsMapper) {
        this.appSettingsRepository = appSettingsRepository;
        this.appSettingsMapper = appSettingsMapper;
    }

    /**
     * Save a appSettings.
     *
     * @param appSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    public AppSettingsDTO save(AppSettingsDTO appSettingsDTO) {
        log.debug("Request to save AppSettings : {}", appSettingsDTO);
        AppSettings appSettings = appSettingsMapper.toEntity(appSettingsDTO);
        appSettings = appSettingsRepository.save(appSettings);
        return appSettingsMapper.toDto(appSettings);
    }

    /**
     * Update a appSettings.
     *
     * @param appSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    public AppSettingsDTO update(AppSettingsDTO appSettingsDTO) {
        log.debug("Request to update AppSettings : {}", appSettingsDTO);
        AppSettings appSettings = appSettingsMapper.toEntity(appSettingsDTO);
        appSettings = appSettingsRepository.save(appSettings);
        return appSettingsMapper.toDto(appSettings);
    }

    /**
     * Partially update a appSettings.
     *
     * @param appSettingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppSettingsDTO> partialUpdate(AppSettingsDTO appSettingsDTO) {
        log.debug("Request to partially update AppSettings : {}", appSettingsDTO);

        return appSettingsRepository
            .findById(appSettingsDTO.getId())
            .map(existingAppSettings -> {
                appSettingsMapper.partialUpdate(existingAppSettings, appSettingsDTO);

                return existingAppSettings;
            })
            .map(appSettingsRepository::save)
            .map(appSettingsMapper::toDto);
    }

    /**
     * Get all the appSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppSettings");
        return appSettingsRepository.findAll(pageable).map(appSettingsMapper::toDto);
    }

    /**
     * Get one appSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppSettingsDTO> findOne(Long id) {
        log.debug("Request to get AppSettings : {}", id);
        return appSettingsRepository.findById(id).map(appSettingsMapper::toDto);
    }

    /**
     * Delete the appSettings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppSettings : {}", id);
        appSettingsRepository.deleteById(id);
    }
}
