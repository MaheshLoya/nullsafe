package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.SpecificNotification;
import com.nullsafe.daily.repository.SpecificNotificationRepository;
import com.nullsafe.daily.service.dto.SpecificNotificationDTO;
import com.nullsafe.daily.service.mapper.SpecificNotificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.SpecificNotification}.
 */
@Service
@Transactional
public class SpecificNotificationService {

    private final Logger log = LoggerFactory.getLogger(SpecificNotificationService.class);

    private final SpecificNotificationRepository specificNotificationRepository;

    private final SpecificNotificationMapper specificNotificationMapper;

    public SpecificNotificationService(
        SpecificNotificationRepository specificNotificationRepository,
        SpecificNotificationMapper specificNotificationMapper
    ) {
        this.specificNotificationRepository = specificNotificationRepository;
        this.specificNotificationMapper = specificNotificationMapper;
    }

    /**
     * Save a specificNotification.
     *
     * @param specificNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecificNotificationDTO save(SpecificNotificationDTO specificNotificationDTO) {
        log.debug("Request to save SpecificNotification : {}", specificNotificationDTO);
        SpecificNotification specificNotification = specificNotificationMapper.toEntity(specificNotificationDTO);
        specificNotification = specificNotificationRepository.save(specificNotification);
        return specificNotificationMapper.toDto(specificNotification);
    }

    /**
     * Update a specificNotification.
     *
     * @param specificNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecificNotificationDTO update(SpecificNotificationDTO specificNotificationDTO) {
        log.debug("Request to update SpecificNotification : {}", specificNotificationDTO);
        SpecificNotification specificNotification = specificNotificationMapper.toEntity(specificNotificationDTO);
        specificNotification = specificNotificationRepository.save(specificNotification);
        return specificNotificationMapper.toDto(specificNotification);
    }

    /**
     * Partially update a specificNotification.
     *
     * @param specificNotificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SpecificNotificationDTO> partialUpdate(SpecificNotificationDTO specificNotificationDTO) {
        log.debug("Request to partially update SpecificNotification : {}", specificNotificationDTO);

        return specificNotificationRepository
            .findById(specificNotificationDTO.getId())
            .map(existingSpecificNotification -> {
                specificNotificationMapper.partialUpdate(existingSpecificNotification, specificNotificationDTO);

                return existingSpecificNotification;
            })
            .map(specificNotificationRepository::save)
            .map(specificNotificationMapper::toDto);
    }

    /**
     * Get all the specificNotifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SpecificNotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpecificNotifications");
        return specificNotificationRepository.findAll(pageable).map(specificNotificationMapper::toDto);
    }

    /**
     * Get all the specificNotifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SpecificNotificationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return specificNotificationRepository.findAllWithEagerRelationships(pageable).map(specificNotificationMapper::toDto);
    }

    /**
     * Get one specificNotification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpecificNotificationDTO> findOne(Long id) {
        log.debug("Request to get SpecificNotification : {}", id);
        return specificNotificationRepository.findOneWithEagerRelationships(id).map(specificNotificationMapper::toDto);
    }

    /**
     * Delete the specificNotification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SpecificNotification : {}", id);
        specificNotificationRepository.deleteById(id);
    }
}
