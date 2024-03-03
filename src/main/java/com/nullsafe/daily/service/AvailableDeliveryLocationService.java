package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.AvailableDeliveryLocation;
import com.nullsafe.daily.repository.AvailableDeliveryLocationRepository;
import com.nullsafe.daily.service.dto.AvailableDeliveryLocationDTO;
import com.nullsafe.daily.service.mapper.AvailableDeliveryLocationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.AvailableDeliveryLocation}.
 */
@Service
@Transactional
public class AvailableDeliveryLocationService {

    private final Logger log = LoggerFactory.getLogger(AvailableDeliveryLocationService.class);

    private final AvailableDeliveryLocationRepository availableDeliveryLocationRepository;

    private final AvailableDeliveryLocationMapper availableDeliveryLocationMapper;

    public AvailableDeliveryLocationService(
        AvailableDeliveryLocationRepository availableDeliveryLocationRepository,
        AvailableDeliveryLocationMapper availableDeliveryLocationMapper
    ) {
        this.availableDeliveryLocationRepository = availableDeliveryLocationRepository;
        this.availableDeliveryLocationMapper = availableDeliveryLocationMapper;
    }

    /**
     * Save a availableDeliveryLocation.
     *
     * @param availableDeliveryLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public AvailableDeliveryLocationDTO save(AvailableDeliveryLocationDTO availableDeliveryLocationDTO) {
        log.debug("Request to save AvailableDeliveryLocation : {}", availableDeliveryLocationDTO);
        AvailableDeliveryLocation availableDeliveryLocation = availableDeliveryLocationMapper.toEntity(availableDeliveryLocationDTO);
        availableDeliveryLocation = availableDeliveryLocationRepository.save(availableDeliveryLocation);
        return availableDeliveryLocationMapper.toDto(availableDeliveryLocation);
    }

    /**
     * Update a availableDeliveryLocation.
     *
     * @param availableDeliveryLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public AvailableDeliveryLocationDTO update(AvailableDeliveryLocationDTO availableDeliveryLocationDTO) {
        log.debug("Request to update AvailableDeliveryLocation : {}", availableDeliveryLocationDTO);
        AvailableDeliveryLocation availableDeliveryLocation = availableDeliveryLocationMapper.toEntity(availableDeliveryLocationDTO);
        availableDeliveryLocation = availableDeliveryLocationRepository.save(availableDeliveryLocation);
        return availableDeliveryLocationMapper.toDto(availableDeliveryLocation);
    }

    /**
     * Partially update a availableDeliveryLocation.
     *
     * @param availableDeliveryLocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AvailableDeliveryLocationDTO> partialUpdate(AvailableDeliveryLocationDTO availableDeliveryLocationDTO) {
        log.debug("Request to partially update AvailableDeliveryLocation : {}", availableDeliveryLocationDTO);

        return availableDeliveryLocationRepository
            .findById(availableDeliveryLocationDTO.getId())
            .map(existingAvailableDeliveryLocation -> {
                availableDeliveryLocationMapper.partialUpdate(existingAvailableDeliveryLocation, availableDeliveryLocationDTO);

                return existingAvailableDeliveryLocation;
            })
            .map(availableDeliveryLocationRepository::save)
            .map(availableDeliveryLocationMapper::toDto);
    }

    /**
     * Get all the availableDeliveryLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AvailableDeliveryLocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AvailableDeliveryLocations");
        return availableDeliveryLocationRepository.findAll(pageable).map(availableDeliveryLocationMapper::toDto);
    }

    /**
     * Get one availableDeliveryLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AvailableDeliveryLocationDTO> findOne(Long id) {
        log.debug("Request to get AvailableDeliveryLocation : {}", id);
        return availableDeliveryLocationRepository.findById(id).map(availableDeliveryLocationMapper::toDto);
    }

    /**
     * Delete the availableDeliveryLocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AvailableDeliveryLocation : {}", id);
        availableDeliveryLocationRepository.deleteById(id);
    }
}
