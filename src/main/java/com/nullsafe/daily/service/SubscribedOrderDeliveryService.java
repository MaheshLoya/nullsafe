package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.SubscribedOrderDelivery;
import com.nullsafe.daily.repository.SubscribedOrderDeliveryRepository;
import com.nullsafe.daily.service.dto.SubscribedOrderDeliveryDTO;
import com.nullsafe.daily.service.mapper.SubscribedOrderDeliveryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.SubscribedOrderDelivery}.
 */
@Service
@Transactional
public class SubscribedOrderDeliveryService {

    private final Logger log = LoggerFactory.getLogger(SubscribedOrderDeliveryService.class);

    private final SubscribedOrderDeliveryRepository subscribedOrderDeliveryRepository;

    private final SubscribedOrderDeliveryMapper subscribedOrderDeliveryMapper;

    public SubscribedOrderDeliveryService(
        SubscribedOrderDeliveryRepository subscribedOrderDeliveryRepository,
        SubscribedOrderDeliveryMapper subscribedOrderDeliveryMapper
    ) {
        this.subscribedOrderDeliveryRepository = subscribedOrderDeliveryRepository;
        this.subscribedOrderDeliveryMapper = subscribedOrderDeliveryMapper;
    }

    /**
     * Save a subscribedOrderDelivery.
     *
     * @param subscribedOrderDeliveryDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscribedOrderDeliveryDTO save(SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO) {
        log.debug("Request to save SubscribedOrderDelivery : {}", subscribedOrderDeliveryDTO);
        SubscribedOrderDelivery subscribedOrderDelivery = subscribedOrderDeliveryMapper.toEntity(subscribedOrderDeliveryDTO);
        subscribedOrderDelivery = subscribedOrderDeliveryRepository.save(subscribedOrderDelivery);
        return subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);
    }

    /**
     * Update a subscribedOrderDelivery.
     *
     * @param subscribedOrderDeliveryDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscribedOrderDeliveryDTO update(SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO) {
        log.debug("Request to update SubscribedOrderDelivery : {}", subscribedOrderDeliveryDTO);
        SubscribedOrderDelivery subscribedOrderDelivery = subscribedOrderDeliveryMapper.toEntity(subscribedOrderDeliveryDTO);
        subscribedOrderDelivery = subscribedOrderDeliveryRepository.save(subscribedOrderDelivery);
        return subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);
    }

    /**
     * Partially update a subscribedOrderDelivery.
     *
     * @param subscribedOrderDeliveryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubscribedOrderDeliveryDTO> partialUpdate(SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO) {
        log.debug("Request to partially update SubscribedOrderDelivery : {}", subscribedOrderDeliveryDTO);

        return subscribedOrderDeliveryRepository
            .findById(subscribedOrderDeliveryDTO.getId())
            .map(existingSubscribedOrderDelivery -> {
                subscribedOrderDeliveryMapper.partialUpdate(existingSubscribedOrderDelivery, subscribedOrderDeliveryDTO);

                return existingSubscribedOrderDelivery;
            })
            .map(subscribedOrderDeliveryRepository::save)
            .map(subscribedOrderDeliveryMapper::toDto);
    }

    /**
     * Get all the subscribedOrderDeliveries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscribedOrderDeliveryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubscribedOrderDeliveries");
        return subscribedOrderDeliveryRepository.findAll(pageable).map(subscribedOrderDeliveryMapper::toDto);
    }

    /**
     * Get all the subscribedOrderDeliveries with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SubscribedOrderDeliveryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return subscribedOrderDeliveryRepository.findAllWithEagerRelationships(pageable).map(subscribedOrderDeliveryMapper::toDto);
    }

    /**
     * Get one subscribedOrderDelivery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscribedOrderDeliveryDTO> findOne(Long id) {
        log.debug("Request to get SubscribedOrderDelivery : {}", id);
        return subscribedOrderDeliveryRepository.findOneWithEagerRelationships(id).map(subscribedOrderDeliveryMapper::toDto);
    }

    /**
     * Delete the subscribedOrderDelivery by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubscribedOrderDelivery : {}", id);
        subscribedOrderDeliveryRepository.deleteById(id);
    }
}
