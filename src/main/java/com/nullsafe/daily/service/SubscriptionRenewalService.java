package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.SubscriptionRenewal;
import com.nullsafe.daily.repository.SubscriptionRenewalRepository;
import com.nullsafe.daily.service.dto.SubscriptionRenewalDTO;
import com.nullsafe.daily.service.mapper.SubscriptionRenewalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.SubscriptionRenewal}.
 */
@Service
@Transactional
public class SubscriptionRenewalService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionRenewalService.class);

    private final SubscriptionRenewalRepository subscriptionRenewalRepository;

    private final SubscriptionRenewalMapper subscriptionRenewalMapper;

    public SubscriptionRenewalService(
        SubscriptionRenewalRepository subscriptionRenewalRepository,
        SubscriptionRenewalMapper subscriptionRenewalMapper
    ) {
        this.subscriptionRenewalRepository = subscriptionRenewalRepository;
        this.subscriptionRenewalMapper = subscriptionRenewalMapper;
    }

    /**
     * Save a subscriptionRenewal.
     *
     * @param subscriptionRenewalDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionRenewalDTO save(SubscriptionRenewalDTO subscriptionRenewalDTO) {
        log.debug("Request to save SubscriptionRenewal : {}", subscriptionRenewalDTO);
        SubscriptionRenewal subscriptionRenewal = subscriptionRenewalMapper.toEntity(subscriptionRenewalDTO);
        subscriptionRenewal = subscriptionRenewalRepository.save(subscriptionRenewal);
        return subscriptionRenewalMapper.toDto(subscriptionRenewal);
    }

    /**
     * Update a subscriptionRenewal.
     *
     * @param subscriptionRenewalDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionRenewalDTO update(SubscriptionRenewalDTO subscriptionRenewalDTO) {
        log.debug("Request to update SubscriptionRenewal : {}", subscriptionRenewalDTO);
        SubscriptionRenewal subscriptionRenewal = subscriptionRenewalMapper.toEntity(subscriptionRenewalDTO);
        subscriptionRenewal = subscriptionRenewalRepository.save(subscriptionRenewal);
        return subscriptionRenewalMapper.toDto(subscriptionRenewal);
    }

    /**
     * Partially update a subscriptionRenewal.
     *
     * @param subscriptionRenewalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubscriptionRenewalDTO> partialUpdate(SubscriptionRenewalDTO subscriptionRenewalDTO) {
        log.debug("Request to partially update SubscriptionRenewal : {}", subscriptionRenewalDTO);

        return subscriptionRenewalRepository
            .findById(subscriptionRenewalDTO.getId())
            .map(existingSubscriptionRenewal -> {
                subscriptionRenewalMapper.partialUpdate(existingSubscriptionRenewal, subscriptionRenewalDTO);

                return existingSubscriptionRenewal;
            })
            .map(subscriptionRenewalRepository::save)
            .map(subscriptionRenewalMapper::toDto);
    }

    /**
     * Get all the subscriptionRenewals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionRenewalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubscriptionRenewals");
        return subscriptionRenewalRepository.findAll(pageable).map(subscriptionRenewalMapper::toDto);
    }

    /**
     * Get one subscriptionRenewal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionRenewalDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionRenewal : {}", id);
        return subscriptionRenewalRepository.findById(id).map(subscriptionRenewalMapper::toDto);
    }

    /**
     * Delete the subscriptionRenewal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionRenewal : {}", id);
        subscriptionRenewalRepository.deleteById(id);
    }
}
