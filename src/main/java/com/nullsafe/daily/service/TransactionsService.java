package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.Transactions;
import com.nullsafe.daily.repository.TransactionsRepository;
import com.nullsafe.daily.service.dto.TransactionsDTO;
import com.nullsafe.daily.service.mapper.TransactionsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.Transactions}.
 */
@Service
@Transactional
public class TransactionsService {

    private final Logger log = LoggerFactory.getLogger(TransactionsService.class);

    private final TransactionsRepository transactionsRepository;

    private final TransactionsMapper transactionsMapper;

    public TransactionsService(TransactionsRepository transactionsRepository, TransactionsMapper transactionsMapper) {
        this.transactionsRepository = transactionsRepository;
        this.transactionsMapper = transactionsMapper;
    }

    /**
     * Save a transactions.
     *
     * @param transactionsDTO the entity to save.
     * @return the persisted entity.
     */
    public TransactionsDTO save(TransactionsDTO transactionsDTO) {
        log.debug("Request to save Transactions : {}", transactionsDTO);
        Transactions transactions = transactionsMapper.toEntity(transactionsDTO);
        transactions = transactionsRepository.save(transactions);
        return transactionsMapper.toDto(transactions);
    }

    /**
     * Update a transactions.
     *
     * @param transactionsDTO the entity to save.
     * @return the persisted entity.
     */
    public TransactionsDTO update(TransactionsDTO transactionsDTO) {
        log.debug("Request to update Transactions : {}", transactionsDTO);
        Transactions transactions = transactionsMapper.toEntity(transactionsDTO);
        transactions = transactionsRepository.save(transactions);
        return transactionsMapper.toDto(transactions);
    }

    /**
     * Partially update a transactions.
     *
     * @param transactionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransactionsDTO> partialUpdate(TransactionsDTO transactionsDTO) {
        log.debug("Request to partially update Transactions : {}", transactionsDTO);

        return transactionsRepository
            .findById(transactionsDTO.getId())
            .map(existingTransactions -> {
                transactionsMapper.partialUpdate(existingTransactions, transactionsDTO);

                return existingTransactions;
            })
            .map(transactionsRepository::save)
            .map(transactionsMapper::toDto);
    }

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        return transactionsRepository.findAll(pageable).map(transactionsMapper::toDto);
    }

    /**
     * Get all the transactions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TransactionsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transactionsRepository.findAllWithEagerRelationships(pageable).map(transactionsMapper::toDto);
    }

    /**
     * Get one transactions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransactionsDTO> findOne(Long id) {
        log.debug("Request to get Transactions : {}", id);
        return transactionsRepository.findOneWithEagerRelationships(id).map(transactionsMapper::toDto);
    }

    /**
     * Delete the transactions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Transactions : {}", id);
        transactionsRepository.deleteById(id);
    }
}
