package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.Refunds;
import com.nullsafe.daily.repository.RefundsRepository;
import com.nullsafe.daily.service.dto.RefundsDTO;
import com.nullsafe.daily.service.mapper.RefundsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.Refunds}.
 */
@Service
@Transactional
public class RefundsService {

    private final Logger log = LoggerFactory.getLogger(RefundsService.class);

    private final RefundsRepository refundsRepository;

    private final RefundsMapper refundsMapper;

    public RefundsService(RefundsRepository refundsRepository, RefundsMapper refundsMapper) {
        this.refundsRepository = refundsRepository;
        this.refundsMapper = refundsMapper;
    }

    /**
     * Save a refunds.
     *
     * @param refundsDTO the entity to save.
     * @return the persisted entity.
     */
    public RefundsDTO save(RefundsDTO refundsDTO) {
        log.debug("Request to save Refunds : {}", refundsDTO);
        Refunds refunds = refundsMapper.toEntity(refundsDTO);
        refunds = refundsRepository.save(refunds);
        return refundsMapper.toDto(refunds);
    }

    /**
     * Update a refunds.
     *
     * @param refundsDTO the entity to save.
     * @return the persisted entity.
     */
    public RefundsDTO update(RefundsDTO refundsDTO) {
        log.debug("Request to update Refunds : {}", refundsDTO);
        Refunds refunds = refundsMapper.toEntity(refundsDTO);
        refunds = refundsRepository.save(refunds);
        return refundsMapper.toDto(refunds);
    }

    /**
     * Partially update a refunds.
     *
     * @param refundsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RefundsDTO> partialUpdate(RefundsDTO refundsDTO) {
        log.debug("Request to partially update Refunds : {}", refundsDTO);

        return refundsRepository
            .findById(refundsDTO.getId())
            .map(existingRefunds -> {
                refundsMapper.partialUpdate(existingRefunds, refundsDTO);

                return existingRefunds;
            })
            .map(refundsRepository::save)
            .map(refundsMapper::toDto);
    }

    /**
     * Get all the refunds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RefundsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Refunds");
        return refundsRepository.findAll(pageable).map(refundsMapper::toDto);
    }

    /**
     * Get one refunds by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RefundsDTO> findOne(Integer id) {
        log.debug("Request to get Refunds : {}", id);
        return refundsRepository.findById(id).map(refundsMapper::toDto);
    }

    /**
     * Delete the refunds by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Integer id) {
        log.debug("Request to delete Refunds : {}", id);
        refundsRepository.deleteById(id);
    }
}
