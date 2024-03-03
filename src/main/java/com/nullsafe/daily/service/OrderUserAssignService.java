package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.OrderUserAssign;
import com.nullsafe.daily.repository.OrderUserAssignRepository;
import com.nullsafe.daily.service.dto.OrderUserAssignDTO;
import com.nullsafe.daily.service.mapper.OrderUserAssignMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.OrderUserAssign}.
 */
@Service
@Transactional
public class OrderUserAssignService {

    private final Logger log = LoggerFactory.getLogger(OrderUserAssignService.class);

    private final OrderUserAssignRepository orderUserAssignRepository;

    private final OrderUserAssignMapper orderUserAssignMapper;

    public OrderUserAssignService(OrderUserAssignRepository orderUserAssignRepository, OrderUserAssignMapper orderUserAssignMapper) {
        this.orderUserAssignRepository = orderUserAssignRepository;
        this.orderUserAssignMapper = orderUserAssignMapper;
    }

    /**
     * Save a orderUserAssign.
     *
     * @param orderUserAssignDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderUserAssignDTO save(OrderUserAssignDTO orderUserAssignDTO) {
        log.debug("Request to save OrderUserAssign : {}", orderUserAssignDTO);
        OrderUserAssign orderUserAssign = orderUserAssignMapper.toEntity(orderUserAssignDTO);
        orderUserAssign = orderUserAssignRepository.save(orderUserAssign);
        return orderUserAssignMapper.toDto(orderUserAssign);
    }

    /**
     * Update a orderUserAssign.
     *
     * @param orderUserAssignDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderUserAssignDTO update(OrderUserAssignDTO orderUserAssignDTO) {
        log.debug("Request to update OrderUserAssign : {}", orderUserAssignDTO);
        OrderUserAssign orderUserAssign = orderUserAssignMapper.toEntity(orderUserAssignDTO);
        orderUserAssign = orderUserAssignRepository.save(orderUserAssign);
        return orderUserAssignMapper.toDto(orderUserAssign);
    }

    /**
     * Partially update a orderUserAssign.
     *
     * @param orderUserAssignDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderUserAssignDTO> partialUpdate(OrderUserAssignDTO orderUserAssignDTO) {
        log.debug("Request to partially update OrderUserAssign : {}", orderUserAssignDTO);

        return orderUserAssignRepository
            .findById(orderUserAssignDTO.getId())
            .map(existingOrderUserAssign -> {
                orderUserAssignMapper.partialUpdate(existingOrderUserAssign, orderUserAssignDTO);

                return existingOrderUserAssign;
            })
            .map(orderUserAssignRepository::save)
            .map(orderUserAssignMapper::toDto);
    }

    /**
     * Get all the orderUserAssigns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderUserAssignDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderUserAssigns");
        return orderUserAssignRepository.findAll(pageable).map(orderUserAssignMapper::toDto);
    }

    /**
     * Get all the orderUserAssigns with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderUserAssignDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderUserAssignRepository.findAllWithEagerRelationships(pageable).map(orderUserAssignMapper::toDto);
    }

    /**
     * Get one orderUserAssign by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderUserAssignDTO> findOne(Long id) {
        log.debug("Request to get OrderUserAssign : {}", id);
        return orderUserAssignRepository.findOneWithEagerRelationships(id).map(orderUserAssignMapper::toDto);
    }

    /**
     * Delete the orderUserAssign by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderUserAssign : {}", id);
        orderUserAssignRepository.deleteById(id);
    }
}
