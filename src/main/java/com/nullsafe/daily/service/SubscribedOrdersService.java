package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.SubscribedOrders;
import com.nullsafe.daily.repository.SubscribedOrdersRepository;
import com.nullsafe.daily.service.dto.SubscribedOrdersDTO;
import com.nullsafe.daily.service.mapper.SubscribedOrdersMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.SubscribedOrders}.
 */
@Service
@Transactional
public class SubscribedOrdersService {

    private final Logger log = LoggerFactory.getLogger(SubscribedOrdersService.class);

    private final SubscribedOrdersRepository subscribedOrdersRepository;

    private final SubscribedOrdersMapper subscribedOrdersMapper;

    public SubscribedOrdersService(SubscribedOrdersRepository subscribedOrdersRepository, SubscribedOrdersMapper subscribedOrdersMapper) {
        this.subscribedOrdersRepository = subscribedOrdersRepository;
        this.subscribedOrdersMapper = subscribedOrdersMapper;
    }

    /**
     * Save a subscribedOrders.
     *
     * @param subscribedOrdersDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscribedOrdersDTO save(SubscribedOrdersDTO subscribedOrdersDTO) {
        log.debug("Request to save SubscribedOrders : {}", subscribedOrdersDTO);
        SubscribedOrders subscribedOrders = subscribedOrdersMapper.toEntity(subscribedOrdersDTO);
        subscribedOrders = subscribedOrdersRepository.save(subscribedOrders);
        return subscribedOrdersMapper.toDto(subscribedOrders);
    }

    /**
     * Update a subscribedOrders.
     *
     * @param subscribedOrdersDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscribedOrdersDTO update(SubscribedOrdersDTO subscribedOrdersDTO) {
        log.debug("Request to update SubscribedOrders : {}", subscribedOrdersDTO);
        SubscribedOrders subscribedOrders = subscribedOrdersMapper.toEntity(subscribedOrdersDTO);
        subscribedOrders = subscribedOrdersRepository.save(subscribedOrders);
        return subscribedOrdersMapper.toDto(subscribedOrders);
    }

    /**
     * Partially update a subscribedOrders.
     *
     * @param subscribedOrdersDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubscribedOrdersDTO> partialUpdate(SubscribedOrdersDTO subscribedOrdersDTO) {
        log.debug("Request to partially update SubscribedOrders : {}", subscribedOrdersDTO);

        return subscribedOrdersRepository
            .findById(subscribedOrdersDTO.getId())
            .map(existingSubscribedOrders -> {
                subscribedOrdersMapper.partialUpdate(existingSubscribedOrders, subscribedOrdersDTO);

                return existingSubscribedOrders;
            })
            .map(subscribedOrdersRepository::save)
            .map(subscribedOrdersMapper::toDto);
    }

    /**
     * Get all the subscribedOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscribedOrdersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubscribedOrders");
        return subscribedOrdersRepository.findAll(pageable).map(subscribedOrdersMapper::toDto);
    }

    /**
     * Get all the subscribedOrders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SubscribedOrdersDTO> findAllWithEagerRelationships(Pageable pageable) {
        return subscribedOrdersRepository.findAllWithEagerRelationships(pageable).map(subscribedOrdersMapper::toDto);
    }

    /**
     * Get one subscribedOrders by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscribedOrdersDTO> findOne(Long id) {
        log.debug("Request to get SubscribedOrders : {}", id);
        return subscribedOrdersRepository.findOneWithEagerRelationships(id).map(subscribedOrdersMapper::toDto);
    }

    /**
     * Delete the subscribedOrders by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubscribedOrders : {}", id);
        subscribedOrdersRepository.deleteById(id);
    }
}
