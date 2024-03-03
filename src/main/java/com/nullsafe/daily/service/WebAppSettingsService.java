package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.WebAppSettings;
import com.nullsafe.daily.repository.WebAppSettingsRepository;
import com.nullsafe.daily.service.dto.WebAppSettingsDTO;
import com.nullsafe.daily.service.mapper.WebAppSettingsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.WebAppSettings}.
 */
@Service
@Transactional
public class WebAppSettingsService {

    private final Logger log = LoggerFactory.getLogger(WebAppSettingsService.class);

    private final WebAppSettingsRepository webAppSettingsRepository;

    private final WebAppSettingsMapper webAppSettingsMapper;

    public WebAppSettingsService(WebAppSettingsRepository webAppSettingsRepository, WebAppSettingsMapper webAppSettingsMapper) {
        this.webAppSettingsRepository = webAppSettingsRepository;
        this.webAppSettingsMapper = webAppSettingsMapper;
    }

    /**
     * Save a webAppSettings.
     *
     * @param webAppSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    public WebAppSettingsDTO save(WebAppSettingsDTO webAppSettingsDTO) {
        log.debug("Request to save WebAppSettings : {}", webAppSettingsDTO);
        WebAppSettings webAppSettings = webAppSettingsMapper.toEntity(webAppSettingsDTO);
        webAppSettings = webAppSettingsRepository.save(webAppSettings);
        return webAppSettingsMapper.toDto(webAppSettings);
    }

    /**
     * Update a webAppSettings.
     *
     * @param webAppSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    public WebAppSettingsDTO update(WebAppSettingsDTO webAppSettingsDTO) {
        log.debug("Request to update WebAppSettings : {}", webAppSettingsDTO);
        WebAppSettings webAppSettings = webAppSettingsMapper.toEntity(webAppSettingsDTO);
        webAppSettings = webAppSettingsRepository.save(webAppSettings);
        return webAppSettingsMapper.toDto(webAppSettings);
    }

    /**
     * Partially update a webAppSettings.
     *
     * @param webAppSettingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WebAppSettingsDTO> partialUpdate(WebAppSettingsDTO webAppSettingsDTO) {
        log.debug("Request to partially update WebAppSettings : {}", webAppSettingsDTO);

        return webAppSettingsRepository
            .findById(webAppSettingsDTO.getId())
            .map(existingWebAppSettings -> {
                webAppSettingsMapper.partialUpdate(existingWebAppSettings, webAppSettingsDTO);

                return existingWebAppSettings;
            })
            .map(webAppSettingsRepository::save)
            .map(webAppSettingsMapper::toDto);
    }

    /**
     * Get all the webAppSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WebAppSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WebAppSettings");
        return webAppSettingsRepository.findAll(pageable).map(webAppSettingsMapper::toDto);
    }

    /**
     * Get one webAppSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WebAppSettingsDTO> findOne(Integer id) {
        log.debug("Request to get WebAppSettings : {}", id);
        return webAppSettingsRepository.findById(id).map(webAppSettingsMapper::toDto);
    }

    /**
     * Delete the webAppSettings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Integer id) {
        log.debug("Request to delete WebAppSettings : {}", id);
        webAppSettingsRepository.deleteById(id);
    }
}
